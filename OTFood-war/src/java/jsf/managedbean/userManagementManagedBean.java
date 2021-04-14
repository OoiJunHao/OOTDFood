/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.OTUserEntitySessionBeanLocal;
import entity.AddressEntity;
import entity.CreditCardEntity;
import entity.MealEntity;
import entity.OTUserEntity;
import entity.ReviewEntity;
import entity.SaleTransactionEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UserExistException;
import util.exception.UserNotFoundException;
import util.security.CryptographicHelper;

/**
 *
 * @author Ong Bik Jeun
 */
@Named(value = "userManagementManagedBean")
@ViewScoped
public class userManagementManagedBean implements Serializable {

    @EJB
    private OTUserEntitySessionBeanLocal oTUserEntitySessionBean;

    private OTUserEntity newUser;
    private OTUserEntity currentUser;
    private String checkingPassword;

    //creditCard
    private List<CreditCardEntity> cards;

    //address
    private List<AddressEntity> address;

    //reviews
    private List<ReviewEntity> reviews;

    /**
     * Creates a new instance of userManagementManagedBean
     */
    public userManagementManagedBean() {
        newUser = new OTUserEntity();
    }

    @PostConstruct
    public void postConstruct() {
        currentUser = (OTUserEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");

        if (currentUser != null) {
            cards = currentUser.getCreditCard();
            address = currentUser.getAddress();
            reviews = currentUser.getReviews();  
        }
    }

    public void createNewUser(ActionEvent event) throws IOException {
        try {
            boolean isSame = checkPassword(getNewUser().getPassword(), checkingPassword);
            if (isSame) {
                Long id = oTUserEntitySessionBean.createNewUser(getNewUser());
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New Account Created", null));
                FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isLogin", true);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentUser", oTUserEntitySessionBean.retrieveUserById(id));
                FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");
            }
        } catch (UserExistException | UnknownPersistenceException | InputDataValidationException | UserNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating new account: " + ex.getMessage(), null));
        }
    }

    private boolean checkPassword(String password, String password1) {
        System.out.println(">>>>>>>>> Checking Password <<<<<<<<");

        String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password1 + getNewUser().getSalt()));
        System.out.println(password);
        System.out.println(passwordHash);

        if (password.equals(passwordHash)) {
            return true;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Password not the same", null));
            return false;
        }
    }

    public OTUserEntity getNewUser() {
        return newUser;
    }

    public void setNewUser(OTUserEntity newUser) {
        this.newUser = newUser;
    }

    public OTUserEntity getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(OTUserEntity currentUser) {
        this.currentUser = currentUser;
    }

    public List<CreditCardEntity> getCards() {
        return cards;
    }

    public void setCards(List<CreditCardEntity> cards) {
        this.cards = cards;
    }

    public List<AddressEntity> getAddress() {
        return address;
    }

    public void setAddress(List<AddressEntity> address) {
        this.address = address;
    }

    public List<ReviewEntity> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewEntity> reviews) {
        this.reviews = reviews;
    }

    public String getCheckingPassword() {
        return checkingPassword;
    }

    public void setCheckingPassword(String checkingPassword) {
        this.checkingPassword = checkingPassword;
    }
}
