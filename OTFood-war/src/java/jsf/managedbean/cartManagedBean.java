/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CreditCardEntitySessionBeanLocal;
import ejb.session.stateless.MealEntitySessionBeanLocal;
import ejb.session.stateless.PromoSessionBeanLocal;
import ejb.session.stateless.SaleTransactionEntitySessionBeanLocal;
import entity.AddressEntity;
import entity.CreditCardEntity;
import entity.MealEntity;
import entity.OTUserEntity;
import entity.PromoCodeEntity;
import entity.SaleTransactionEntity;
//import entity.Order;
import entity.SaleTransactionLineEntity;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.PrimeFaces;
import util.exception.CreateNewSaleTransactionException;
import util.exception.InputDataValidationException;
import util.exception.MealNotFoundException;
import util.exception.PromotionNotFoundException;

/**
 *
 * @author Mitsuki
 */
@Named(value = "cartManagedBean")
@SessionScoped
public class cartManagedBean implements Serializable {

    @EJB
    private PromoSessionBeanLocal promoSessionBean;

    @EJB
    private CreditCardEntitySessionBeanLocal creditCardEntitySessionBean;

    @EJB
    private MealEntitySessionBeanLocal mealEntitySessionBean;

    @EJB
    private SaleTransactionEntitySessionBeanLocal saleTransactionEntitySessionBean;

    private OTUserEntity currentUser;
    private SaleTransactionLineEntity selectedItem;
//    private List<Order> orders;
    private List<SaleTransactionLineEntity> lineItems;
    private SaleTransactionEntity order;

    private BigDecimal totalAmount;
    private int amtToCart;

    private List<CreditCardEntity> creditCards;
    private CreditCardEntity selectedCard;

    private List<AddressEntity> address;
    private AddressEntity selectedAddress;

    private String promoCode;

    boolean checkoutComplete;

    boolean isEmpty;

    /**
     * Creates a new instance of cartManagedBean
     */
    public cartManagedBean() {
        //orders = new ArrayList<>();
        lineItems = new ArrayList<>();
        amtToCart = 0;
        totalAmount = new BigDecimal("0.00");

    }

    @PostConstruct
    public void postConstruct() {
        try {
            getFakeData();
            System.out.println(">>>>> FINISH <<<<<<");
        } catch (MealNotFoundException ex) {
            Logger.getLogger(cartManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void addToCart(MealEntity meal) {
        //checks if the meal already exists in the cart;
        int currentIndexInCart = existInCart(meal);

        if (amtToCart == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select at least 1 item.", null));
        } else {
            if (currentIndexInCart == -1) {
//                orders.add(new Order(meal, amtToCart));
                lineItems.add(new SaleTransactionLineEntity(meal, amtToCart));
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Added to cart!.", null));
            } else {
//                int qty = orders.get(currentIndexInCart).getQuantity() + amtToCart;
//                orders.get(currentIndexInCart).setQuantity(qty);
                int qty = lineItems.get(currentIndexInCart).getQuantity() + amtToCart;
                lineItems.get(currentIndexInCart).setQuantity(qty);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Quantity updated!.", null));
            }
            amtToCart = 0;
        }
        totalAmount();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cartEmpty", false);
    }

    private int existInCart(MealEntity meal) {
//        for (int i = 0; i < orders.size(); i++) {
//            if (orders.get(i).getMeal().getMealId() == meal.getMealId()) {
//                return i;
//            }
//        }
        for (int i = 0; i < lineItems.size(); i++) {
            if (lineItems.get(i).getMeal().getMealId() == meal.getMealId()) {
                return i;
            }
        }
        return -1;
    }

    public BigDecimal totalAmount() {
        System.out.println(">>>>>>> Total Amount <<<<<<<<");
//        for (Order ord : this.orders) {
//            totalAmount = totalAmount.add(ord.getMeal().getPrice().multiply(BigDecimal.valueOf(ord.getQuantity())));
//        }
        for (SaleTransactionLineEntity ord : this.lineItems) {
            totalAmount = totalAmount.add(ord.getMeal().getPrice().multiply(BigDecimal.valueOf(ord.getQuantity())));
        }
        System.out.println(totalAmount);
        return totalAmount;
    }

    public void removeFromCart(ActionEvent event) {
        System.out.println(">>>>> Removing!!! <<<<<<<");
        MealEntity meal = (MealEntity) event.getComponent().getAttributes().get("bento");
        int currentIndexInCart = existInCart(meal);
//        orders.remove(currentIndexInCart);
        lineItems.remove(currentIndexInCart);

        for (int i = 0; i < lineItems.size(); i++) {
            System.out.println(lineItems.get(i).getMeal().getName());

        }

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Meal removed!.", null));

        if (lineItems.isEmpty()) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cartEmpty", true);
        }
    }

    public void updateOrderList(ActionEvent event) {
        System.out.println(">>>>>>> Updating!!!! <<<<<<<<");
        System.out.println("Selected item details:");
        System.out.println(selectedItem.getQuantity());
        int index = existInCart(selectedItem.getMeal());
        if (index != -1) {
            lineItems.set(index, selectedItem);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item quantity updated!", null));

        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Item not found", null));
        }
        for (int i = 0; i < lineItems.size(); i++) {
            System.out.println(lineItems.get(i).getMeal().getName() + " " + lineItems.get(i).getQuantity());

        }
        PrimeFaces.current().executeScript("PF('editItemsDialog').hide()");
        PrimeFaces.current().ajax().update("form:checkoutForm");

    }

    public void checkPromoCode(ActionEvent event) {
        Boolean valid = false;
        try {
            PromoCodeEntity promo = promoSessionBean.retrieveCodeByDiscountCode(promoCode);

            valid = true;
        } catch (PromotionNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Promo Code!", null));
        } finally {
            if (valid) {
                try {
                    PromoCodeEntity promo = promoSessionBean.retrieveCodeByDiscountCode(promoCode);

                    Boolean reallyValid = promoSessionBean.checkPromoCode(promoCode);

                    if (reallyValid) {
                        switch (promo.getDiscountCodeTypeEnum()) {
                            case FLAT:
                                totalAmount = totalAmount.subtract(promo.getDiscountRate());
                                break;

                            case PERCENTAGE:
                                totalAmount = totalAmount.multiply(new BigDecimal("1.00").subtract(promo.getDiscountRate()));
                                break;
                        }

                    } else {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Invalid Promo Code!", null));
                    }
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Promo Code Applied!", null));

                } catch (PromotionNotFoundException ex) {
                    Logger.getLogger(cartManagedBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    public void createNewOrder() {
        if (lineItems.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Please add items to cart!", null));
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cartEmpty", true);
        } else {
            int totalQuantity = 0;
            for (int i = 0; i < lineItems.size(); i++) {
                totalQuantity += lineItems.get(i).getQuantity();
            }
            SaleTransactionEntity txn = new SaleTransactionEntity(lineItems.size(), totalQuantity, totalAmount, new Date(), new Date());
            txn.setUser(currentUser);
            txn.setSaleTransactionLineItemEntities(lineItems);

            Boolean success = false;
            try {
                PromoCodeEntity promo = promoSessionBean.retrieveCodeByDiscountCode(promoCode);
                Boolean reallyValid = promoSessionBean.checkPromoCode(promoCode);
                success = true;

                if (promo != null && success && reallyValid) {
                    txn.setPromoCode(promoSessionBean.retrieveCodeByDiscountCode(promoCode));
                }
            } catch (PromotionNotFoundException ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, ex.getMessage(), null));
            }

            txn.setTotalAmount(totalAmount);
            txn.setTotalLineItem(lineItems.size());

            try {
                saleTransactionEntitySessionBean.createNewSaleTransaction(currentUser.getUserId(), selectedCard.getCreditCardId(), selectedAddress.getAddressId(), txn);
            } catch (CreateNewSaleTransactionException | InputDataValidationException ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, ex.getMessage(), null));
            }
            setCheckoutComplete(true);
        }
    }

    private void getFakeData() throws MealNotFoundException {
        System.out.println(">>>>>> fake data <<<<<<<<");
        SaleTransactionLineEntity order1 = new SaleTransactionLineEntity(mealEntitySessionBean.retrieveMealById(1l), 2);
        SaleTransactionLineEntity order2 = new SaleTransactionLineEntity(mealEntitySessionBean.retrieveMealById(2l), 1);
        SaleTransactionLineEntity order3 = new SaleTransactionLineEntity(mealEntitySessionBean.retrieveMealById(3l), 4);
        SaleTransactionLineEntity order4 = new SaleTransactionLineEntity(mealEntitySessionBean.retrieveMealById(4l), 3);
//        orders.add(order1);
//        orders.add(order2);
//        orders.add(order3);
//        orders.add(order4);
        lineItems.add(order1);
        lineItems.add(order2);
        lineItems.add(order3);
        lineItems.add(order4);

        System.out.println(lineItems.size());

        for (int i = 0; i < lineItems.size(); i++) {
            System.out.println(lineItems.get(i).getMeal().getName());

        }
        totalAmount();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cartEmpty", false);
    }

    //after checkout complete
    public void removeEverything() {
        lineItems.clear();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(cartManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cartEmpty", true);
    }

    public void directToCheckout(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/userPages/checkout.xhtml");
        setCurrentUser((OTUserEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser"));

        if (getCurrentUser() != null) {
            setCreditCards(getCurrentUser().getCreditCard());
            setAddress(getCurrentUser().getAddress());
        }

        setCheckoutComplete(false);
        setIsEmpty(creditCards.isEmpty() && address.isEmpty());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Please select payment method", null));
    }

    public void getMealOrderDetails(ActionEvent event) {
        System.out.println(">>>>> INDIVIDUAL MEAL ORDER DETAILS <<<<<<");
        selectedItem = (SaleTransactionLineEntity) event.getComponent().getAttributes().get("meal");
        System.out.println("Meal Item = " + selectedItem.getMeal().getName());
    }

//    public void checkIfEmpty() {
//        System.out.println(">>>> checking <<<<<<<");
//        System.out.println(creditCards.isEmpty());
//        System.out.println(creditCards.get(0));
//        setIsEmpty(creditCards.isEmpty() && address.isEmpty());
//    }
    public OTUserEntity getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(OTUserEntity currentUser) {
        this.currentUser = currentUser;
    }

//    public List<Order> getOrders() {
//        return orders;
//    }
//
//    public void setOrders(List<Order> orders) {
//        this.orders = orders;
//    }
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getAmtToCart() {
        return amtToCart;
    }

    public void setAmtToCart(int amtToCart) {
        this.amtToCart = amtToCart;
    }

    public List<SaleTransactionLineEntity> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<SaleTransactionLineEntity> lineItems) {
        this.lineItems = lineItems;
    }

    public SaleTransactionLineEntity getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(SaleTransactionLineEntity selectedItem) {
        this.selectedItem = selectedItem;
    }

    public List<CreditCardEntity> getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(List<CreditCardEntity> creditCards) {
        this.creditCards = creditCards;
    }

    public CreditCardEntity getSelectedCard() {
        return selectedCard;
    }

    public void setSelectedCard(CreditCardEntity card) {
        this.selectedCard = card;
    }

    public boolean isCheckoutComplete() {
        return checkoutComplete;
    }

    public void setCheckoutComplete(boolean checkoutComplete) {
        this.checkoutComplete = checkoutComplete;
    }

    public SaleTransactionEntity getOrder() {
        return order;
    }

    public void setOrder(SaleTransactionEntity order) {
        this.order = order;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public List<AddressEntity> getAddress() {
        return address;
    }

    public void setAddress(List<AddressEntity> address) {
        this.address = address;
    }

    public AddressEntity getSelectedAddress() {
        return selectedAddress;
    }

    public void setSelectedAddress(AddressEntity selectedAddress) {
        this.selectedAddress = selectedAddress;
    }

    public boolean isIsEmpty() {
        return isEmpty;
    }

    public void setIsEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

}
