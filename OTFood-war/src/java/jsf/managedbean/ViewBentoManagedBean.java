package jsf.managedbean;

import ejb.session.stateless.MealEntitySessionBeanLocal;
import ejb.session.stateless.ReviewEntitySessionBeanLocal;
import entity.BentoEntity;
import entity.MealEntity;
import entity.OTUserEntity;
import entity.ReviewEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.TabChangeEvent;
import util.enumeration.CategoryEnum;
import util.exception.InputDataValidationException;
import util.exception.MealNotFoundException;
import util.exception.ReviewExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UserNotFoundException;

/**
 *
 * @author yuntiangu
 */
@Named(value = "viewBentoManagedBean")
@ViewScoped
public class ViewBentoManagedBean implements Serializable {

    @EJB
    private ReviewEntitySessionBeanLocal reviewEntitySessionBeanLocal;

    @EJB
    private MealEntitySessionBeanLocal mealEntitySessionBeanLocal;

    @Inject
    private cartManagedBean cartManagedBean;

    private List<BentoEntity> listOfBentos;
    private List<BentoEntity> listOfAllBentos;
    private List<CategoryEnum> listOfCategories;
    private String selectedCategory;
    private BentoEntity selectedBento;
    private int selectedBentoQuantity;

    private List<ReviewEntity> currentBentoReviews;

    private int rating;
    private String review;

    @PostConstruct
    public void postConstruct() {
        this.currentBentoReviews = new ArrayList<>();
        this.listOfCategories = new ArrayList<>();
        this.listOfCategories.add(CategoryEnum.CHICKEN);
        this.listOfCategories.add(CategoryEnum.FISH);
        this.listOfCategories.add(CategoryEnum.BEEF);
        this.listOfCategories.add(CategoryEnum.PORK);
        this.listOfCategories.add(CategoryEnum.VEGETARIAN);
        this.listOfCategories.add(CategoryEnum.VEGAN);
        this.listOfCategories.add(CategoryEnum.BALANCED);
        this.setListOfBentos(mealEntitySessionBeanLocal.retrieveBentosByCategory("CHICKEN"));
        this.setListOfAllBentos(mealEntitySessionBeanLocal.retriveAllBentos());
    }

    public ViewBentoManagedBean() {
        this.selectedBento = new BentoEntity();
        this.selectedBentoQuantity = 1;
        this.selectedCategory = "Chicken";
        this.rating = 1;
        this.review = "";
    }

    public void refreshListByTabSelected(TabChangeEvent event) {
        System.out.println("List Refreshed");
        String currentTabId = event.getTab().getTitle(); //This will be the CategoryEnum
        System.out.println("tab Title: " + currentTabId);
        setSelectedCategory(event.getTab().getTitle());
        System.out.println("current category " + getSelectedCategory());
        setListOfBentos(mealEntitySessionBeanLocal.retrieveBentosByCategory(currentTabId));
        System.out.println("No of Bentos: " + this.listOfBentos.toString());
    }

    public void getListOfBentosByCategory(AjaxBehaviorEvent event) {
        this.setListOfBentos(mealEntitySessionBeanLocal.retrieveBentosByCategory(getSelectedCategory()));
    }

    public void addBentoToCart(ActionEvent event) {
        cartManagedBean.setAmtToCart(selectedBentoQuantity);
        cartManagedBean.addToCart(selectedBento);
        selectedBentoQuantity = 1;
        PrimeFaces.current().ajax().update("cartForm");
    }

    public void addSingleBentoToCart(ActionEvent event) {
        BentoEntity selectedBento = (BentoEntity) event.getComponent().getAttributes().get("selected");
        cartManagedBean.setAmtToCart(1);
        cartManagedBean.addToCart(selectedBento);
        PrimeFaces.current().ajax().update("cartForm");
    }

    public void loadBentoReviews() {
        currentBentoReviews = new ArrayList<>();
        selectedBento.getReviews().size();
        currentBentoReviews = selectedBento.getReviews();

        class comp implements Comparator<ReviewEntity> {
            @Override
            public int compare(ReviewEntity o1, ReviewEntity o2) {
                if (o1.getReviewDate().after(o2.getReviewDate())) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }

        this.currentBentoReviews.sort(new comp());

        System.out.println("reviews: " + currentBentoReviews);
    }

    public void addReview(ActionEvent event) {
        try {
            ReviewEntity reviewToAdd = new ReviewEntity(this.rating, this.review, new Date());
            OTUserEntity user = (OTUserEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
            reviewEntitySessionBeanLocal.addReview(reviewToAdd, user.getUserId(), this.selectedBento.getMealId());
            this.review = "";
            this.rating = 1;
            this.selectedBento = (BentoEntity) mealEntitySessionBeanLocal.retrieveMealById(this.selectedBento.getMealId());
            this.loadBentoReviews();
            System.out.println("CURRENTLTY ON ---> " + this.selectedBento.getName());
            System.out.println("HOW MANY REVIEWSS WTFF ---------> " + this.selectedBento.getReviews().size());
            System.out.println("HOW MANY REVIEWSS ---------> " + this.currentBentoReviews.size());

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Review successfully added!", null));
        } catch (ReviewExistException | UnknownPersistenceException | UserNotFoundException | InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error has occured: " + ex.getMessage(), null));
        } catch (MealNotFoundException ex) {
            Logger.getLogger(ViewBentoManagedBean.class.getName()).log(Level.SEVERE, null, ex);//will never hit this
        }
    }

    /**
     * @return the listOfBentos
     */
    public List<BentoEntity> getListOfBentos() {
        return listOfBentos;
    }

    /**
     * @param listOfBentos the listOfBentos to set
     */
    public void setListOfBentos(List<BentoEntity> listOfBentos) {
        this.listOfBentos = listOfBentos;
    }

    /**
     * @return the listOfCategories
     */
    public List<CategoryEnum> getListOfCategories() {
        return listOfCategories;
    }

    /**
     * @param listOfCategories the listOfCategories to set
     */
    public void setListOfCategories(List<CategoryEnum> listOfCategories) {
        this.listOfCategories = listOfCategories;
    }

    /**
     * @return the selectedCategory
     */
    public String getSelectedCategory() {
        return selectedCategory;
    }

    /**
     * @param selectedCategory the selectedCategory to set
     */
    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    /**
     * @return the listOfAllBentos
     */
    public List<BentoEntity> getListOfAllBentos() {
        return listOfAllBentos;
    }

    /**
     * @param listOfAllBentos the listOfAllBentos to set
     */
    public void setListOfAllBentos(List<BentoEntity> listOfAllBentos) {
        this.listOfAllBentos = listOfAllBentos;
    }

    /**
     * @return the selectedBento
     */
    public BentoEntity getSelectedBento() {
        //System.out.println("current selected bento: " + selectedBento);
        return selectedBento;
    }

    /**
     * @param selectedBento the selectedBento to set
     */
    public void setSelectedBento(BentoEntity selectedBento) {
        this.selectedBento = selectedBento;
    }

    /**
     * @return the selectedBentoQuantity
     */
    public int getSelectedBentoQuantity() {
        return selectedBentoQuantity;
    }

    /**
     * @param selectedBentoQuantity the selectedBentoQuantity to set
     */
    public void setSelectedBentoQuantity(int selectedBentoQuantity) {
        System.out.println("Setting selectedBentoQuantity: " + selectedBentoQuantity);
        this.selectedBentoQuantity = selectedBentoQuantity;
    }

    /**
     * @return the currentBentoReviews
     */
    public List<ReviewEntity> getCurrentBentoReviews() {
        return currentBentoReviews;
    }

    /**
     * @param currentBentoReviews the currentBentoReviews to set
     */
    public void setCurrentBentoReviews(List<ReviewEntity> currentBentoReviews) {
        this.currentBentoReviews = currentBentoReviews;
    }

    /**
     * @return the rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * @param rating the rating to set
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * @return the review
     */
    public String getReview() {
        return review;
    }

    /**
     * @param review the review to set
     */
    public void setReview(String review) {
        this.review = review;
    }
}
