/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.MealEntitySessionBeanLocal;
import ejb.session.stateless.PromoSessionBeanLocal;
import ejb.session.stateless.SaleTransactionEntitySessionBeanLocal;
import entity.AddressEntity;
import entity.CreditCardEntity;
import entity.MealEntity;
import entity.OTUserEntity;
import entity.PromoCodeEntity;
import entity.SaleTransactionEntity;
import entity.SaleTransactionLineEntity;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
    private MealEntitySessionBeanLocal mealEntitySessionBean;

    @EJB
    private SaleTransactionEntitySessionBeanLocal saleTransactionEntitySessionBean;

    private OTUserEntity currentUser;
    private SaleTransactionLineEntity selectedItem;

    private List<SaleTransactionLineEntity> lineItems;

    private BigDecimal totalAmount;
    private int amtToCart;

    private List<String> creditCardNumbers;
    private List<CreditCardEntity> creditCards;
    private String selectedCard;

    private List<String> addressNames;
    private List<AddressEntity> address;
    private String selectedAddress;

    private Date deliveryDateTime;

    private String promoCode;
    private PromoCodeEntity appliedPromoCode;

    private boolean missingDetails;
    private boolean checkoutComplete;

    public cartManagedBean() {
        this.creditCards = new ArrayList<>();
        this.creditCardNumbers = new ArrayList<>();
        this.address = new ArrayList<>();
        this.addressNames = new ArrayList<>();
        this.lineItems = new ArrayList<>();
        this.amtToCart = 0;
        this.totalAmount = new BigDecimal("0.00");
        this.missingDetails = true;
        this.checkoutComplete = false;
        this.promoCode = "";

        // Create placeholder for DeliveryDateAndTimePicker
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MINUTE, 0);
        c.add(Calendar.DAY_OF_MONTH, 1);
        this.deliveryDateTime = c.getTime();
    }

    @PostConstruct
    public void postConstruct() {  // To be empty eventually
       
    }

    public void addToCart(MealEntity meal) {
        int currentIndexInCart = existInCart(meal);

        if (amtToCart == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select at least 1 item.", null));
        } else {
            if (currentIndexInCart == -1) {
                lineItems.add(new SaleTransactionLineEntity(meal, amtToCart));
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Added to cart!", null));
            } else {
                int qty = lineItems.get(currentIndexInCart).getQuantity() + amtToCart;
                lineItems.get(currentIndexInCart).setQuantity(qty);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Quantity updated!", null));
            }
            amtToCart = 0;
        }
        totalAmount();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cartEmpty", false);
    }
    
    
    // only used by CYOBManagedBean
    public void addCYOBToCart(MealEntity meal) {
        lineItems.add(new SaleTransactionLineEntity(meal, 1));
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Added to cart!", null));
        totalAmount();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cartEmpty", false);
    }

    private int existInCart(MealEntity meal) {
        for (int i = 0; i < lineItems.size(); i++) {
            if (Objects.equals(lineItems.get(i).getMeal().getMealId(), meal.getMealId())) {
                return i;
            }
        }
        return -1;
    }

    public BigDecimal totalAmount() {
        System.out.println(">>>>>>> changing total amount <<<<<<<");
        BigDecimal total = new BigDecimal(0.0);
        for (SaleTransactionLineEntity ord : this.lineItems) {
            total = total.add(ord.getMeal().getPrice().multiply(BigDecimal.valueOf(ord.getQuantity())));
        }
        if (this.appliedPromoCode != null) {
            switch (this.appliedPromoCode.getDiscountCodeTypeEnum()) {
                case FLAT:
                    total = total.subtract(this.appliedPromoCode.getDiscountRate());
                    break;

                case PERCENTAGE:
                    total = total.multiply(new BigDecimal(1.0).subtract(new BigDecimal("0.01").multiply(this.appliedPromoCode.getDiscountRate()))).setScale(2, BigDecimal.ROUND_HALF_EVEN);
                    break;
            }
        }
        this.totalAmount = total;
        System.out.println(totalAmount);
        return this.totalAmount;
    }

    public void removeFromCart(ActionEvent event) {
        System.out.println(">>>>> Removing!!! <<<<<<<");
        MealEntity meal = (MealEntity) event.getComponent().getAttributes().get("bento");
        int currentIndexInCart = existInCart(meal);
        lineItems.remove(currentIndexInCart);
        for (int i = 0; i < lineItems.size(); i++) {
            System.out.println(lineItems.get(i).getMeal().getName());
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Meal removed!", null));
        totalAmount();
        PrimeFaces.current().ajax().update("cartForm");
        PrimeFaces.current().ajax().update("checkoutForm");
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
        totalAmount();
        PrimeFaces.current().executeScript("PF('editItemsDialog').hide()");
        PrimeFaces.current().ajax().update("cartForm");
    }

    public void removePromoCode(ActionEvent event) {
        this.promoCode = "";
        this.appliedPromoCode = null;
        this.totalAmount = totalAmount();
        PrimeFaces.current().ajax().update("cartForm");
    }

    public void checkPromoCode(ActionEvent event) {
        try {
            System.out.println(">>>>>>>>>>>>" + this.promoCode);

            PromoCodeEntity promo = promoSessionBean.retrieveCodeByDiscountCode(promoCode);
            Boolean reallyValid = promoSessionBean.checkPromoCode(promoCode);
            if (reallyValid) {
                switch (promo.getDiscountCodeTypeEnum()) {
                    case FLAT:
                        totalAmount = totalAmount().subtract(promo.getDiscountRate());
                        break;

                    case PERCENTAGE:
                        totalAmount = totalAmount().multiply(new BigDecimal(1.0).subtract(new BigDecimal("0.01").multiply(promo.getDiscountRate()))).setScale(2, BigDecimal.ROUND_HALF_EVEN);
                        break;
                }
                this.appliedPromoCode = promo;
                PrimeFaces.current().ajax().update("cartForm");
                System.out.println("Aplied promo coed:   " + this.appliedPromoCode.getDiscountCode());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Promo Code Applied!", null));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Promo Code has expired!", null));
            }
        } catch (PromotionNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid Promo Code!", null));
        }

    }

    public void createNewOrder(ActionEvent action) {

        System.out.println("check    " + this.selectedCard);
        System.out.println(totalAmount);
        int totalQuantity = 0;
        for (int i = 0; i < lineItems.size(); i++) {
            totalQuantity += lineItems.get(i).getQuantity();
        }
        SaleTransactionEntity txn = new SaleTransactionEntity(lineItems.size(), totalQuantity, totalAmount, new Date(), this.deliveryDateTime); //to add delieary dat dime
        txn.setUser(currentUser);
        txn.setSaleTransactionLineItemEntities(lineItems);

        txn.setTotalAmount(totalAmount);
        txn.setTotalLineItem(lineItems.size());

        // Obtain corresponding Credit Card Entity
        long ccId = 0l;
        for (CreditCardEntity cc : this.creditCards) {
            if (cc.getCardNumber().equals(this.selectedCard)) {
                ccId = cc.getCreditCardId();
                break;
            }
        }

        // Obtain corresponding Address Entity
        long addressId = 0l;
        for (AddressEntity add : this.address) {
            if (add.getAddress().equals(this.selectedAddress)) {
                addressId = add.getAddressId();
                break;
            }
        }

        try {
            if (this.appliedPromoCode != null) {
                long saleTransactionId = saleTransactionEntitySessionBean.createNewSaleTransactionWithPromo(currentUser.getUserId(), ccId, addressId, this.appliedPromoCode.getPromoCodeId(), txn);
                System.out.println("Successfully checked out: Sale Transaction ID " + saleTransactionId);
                System.out.println(totalAmount);
                PrimeFaces.current().executeScript("PF('dialogPaySuccess').show()");
//                removeEverything();
            } else {
                long saleTransactionId = saleTransactionEntitySessionBean.createNewSaleTransaction(currentUser.getUserId(), ccId, addressId, txn);
                System.out.println("Successfully checked out: Sale Transaction ID " + saleTransactionId);
                System.out.println(totalAmount);
                PrimeFaces.current().executeScript("PF('dialogPaySuccess').show()");
//                removeEverything();
            }
        } catch (CreateNewSaleTransactionException | InputDataValidationException ex) {
            PrimeFaces.current().executeScript("PF('dialogPayFail').show()");
        }

//        PrimeFaces.current().executeScript("PF('dialogPay').show()");
    }

    //after checkout complete
    public void removeEverything() {
        System.out.println(">>>>>>> RESETTING DATA <<<<<<<<<");
        this.lineItems.clear();
        System.out.println(lineItems.isEmpty());
        this.totalAmount = new BigDecimal(0.0);
        this.promoCode = "";
        this.appliedPromoCode = null;
        this.checkoutComplete = true;
        PrimeFaces.current().ajax().update("cartForm");

    }

    public void directToHome() {
        removeEverything();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(cartManagedBean.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void directToCheckout(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/userPages/checkout.xhtml");
        setCurrentUser((OTUserEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser"));
        if (getCurrentUser() != null) {
            this.creditCards = getCurrentUser().getCreditCard();
            this.address = getCurrentUser().getAddress();
            // Generate list of credit card numbers as strings for display
            for (CreditCardEntity cc : this.creditCards) {
                this.getCreditCardNumbers().add(cc.getCardNumber());
            }
            // Generate list of adress names as string for display
            for (AddressEntity add : this.address) {
                this.getAddressNames().add(add.getAddress());
            }
        }
        this.setMissingDetails(this.creditCards.isEmpty() || this.address.isEmpty());
    }

    public void getMealOrderDetails(ActionEvent event) {
        System.out.println(">>>>> INDIVIDUAL MEAL ORDER DETAILS <<<<<<");
        this.selectedItem = (SaleTransactionLineEntity) event.getComponent().getAttributes().get("meal");
        System.out.println("Meal Item = " + this.selectedItem.getMeal().getName());
    }

    public OTUserEntity getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(OTUserEntity currentUser) {
        this.currentUser = currentUser;
    }

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

    public String getSelectedCard() {
        return selectedCard;
    }

    public void setSelectedCard(String card) {
        this.selectedCard = card;
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

    public String getSelectedAddress() {
        return selectedAddress;
    }

    public void setSelectedAddress(String selectedAddress) {
        this.selectedAddress = selectedAddress;
    }

    /**
     * @return the creditCardNumbers
     */
    public List<String> getCreditCardNumbers() {
        return creditCardNumbers;
    }

    /**
     * @param creditCardNumbers the creditCardNumbers to set
     */
    public void setCreditCardNumbers(List<String> creditCardNumbers) {
        this.creditCardNumbers = creditCardNumbers;
    }

    /**
     * @return the addressNames
     */
    public List<String> getAddressNames() {
        return addressNames;
    }

    /**
     * @param addressNames the addressNames to set
     */
    public void setAddressNames(List<String> addressNames) {
        this.addressNames = addressNames;
    }

    /**
     * @return the deliveryDateTime
     */
    public Date getDeliveryDateTime() {
        return deliveryDateTime;
    }

    /**
     * @param deliveryDateTime the deliveryDateTime to set
     */
    public void setDeliveryDateTime(Date deliveryDateTime) {
        this.deliveryDateTime = deliveryDateTime;
    }

    /**
     * @return the missingDetails
     */
    public boolean isMissingDetails() {
        return missingDetails;
    }

    /**
     * @param missingDetails the missingDetails to set
     */
    public void setMissingDetails(boolean missingDetails) {
        this.missingDetails = missingDetails;
    }

    /**
     * @return the appliedPromoCode
     */
    public PromoCodeEntity getAppliedPromoCode() {
        return appliedPromoCode;
    }

    /**
     * @param appliedPromoCode the appliedPromoCode to set
     */
    public void setAppliedPromoCode(PromoCodeEntity appliedPromoCode) {
        this.appliedPromoCode = appliedPromoCode;
    }

    public boolean isCheckoutComplete() {
        return checkoutComplete;
    }

    public void setCheckoutComplete(boolean checkoutComplete) {
        this.checkoutComplete = checkoutComplete;
    }

}
