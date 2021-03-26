/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.AddressEntitySessionBeanLocal;
import ejb.session.stateless.CreditCardEntitySessionBeanLocal;
import ejb.session.stateless.OTUserEntitySessionBeanLocal;
import entity.AddressEntity;
import entity.CreditCardEntity;
import entity.OTUserEntity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import util.enumeration.RegionEnum;
import util.exception.AddressExistException;
import util.exception.CreditCardExistException;
import util.exception.CreditCardNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.NoAddressFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateUserException;
import util.exception.UserNotFoundException;
import util.security.CryptographicHelper;

/**
 *
 * @author benny
 */
@Named(value = "profileManagedBean")
@ViewScoped
public class ProfileManagedBean implements Serializable {

    @EJB
    private AddressEntitySessionBeanLocal addressEntitySessionBeanLocal;

    @EJB
    private CreditCardEntitySessionBeanLocal creditCardEntitySessionBeanLocal;

    @EJB
    private OTUserEntitySessionBeanLocal oTUserEntitySessionBeanLocal;

    /**
     * Creates a new instance of ProfileManagedBean
     */
    private OTUserEntity profile; // purely used for static field updates (not for related entity manipulation)
    private String profileDate;
    private List<AddressEntity> addresses;
    private List<CreditCardEntity> creditCards;
    private AddressEntity newAddress;
    private CreditCardEntity newCreditCard;
    private List<RegionEnum> regions;
    private RegionEnum region;

    //to change password
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

    public ProfileManagedBean() {
        addresses = new ArrayList<>();
        creditCards = new ArrayList<>();
        regions = new ArrayList<>();
        newAddress = new AddressEntity();
        newCreditCard = new CreditCardEntity();
    }

    @PostConstruct
    public void postConstruct() {
        profile = (OTUserEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateInString = formatter.format(profile.getDob());
        profileDate = dateInString;
        try {
            Date newDate = formatter.parse(dateInString);
            profile.setDob(newDate);
        } catch (ParseException ex) {
            Logger.getLogger(ProfileManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        addresses = addressEntitySessionBeanLocal.retrieveAddressesByUserId(profile.getUserId());
        creditCards = creditCardEntitySessionBeanLocal.retrieveAllCardByUser(profile.getUserId());
        regions = Arrays.asList(RegionEnum.values());

    }

    public void handleFileUpload(FileUploadEvent event) throws InputDataValidationException, UpdateUserException, UserNotFoundException {

        try {
            String newFilePath = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("alternatedocroot_1") + System.getProperty("file.separator") + event.getFile().getFileName();

            System.err.println("********** Demo03ManagedBean.handleFileUpload(): File name: " + event.getFile().getFileName());
            System.err.println("********** Demo03ManagedBean.handleFileUpload(): newFilePath: " + newFilePath);

            File file = new File(newFilePath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            int a;
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];

            InputStream inputStream = event.getFile().getInputStream();

            while (true) {
                a = inputStream.read(buffer);

                if (a < 0) {
                    break;
                }

                fileOutputStream.write(buffer, 0, a);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();
            String newFile = event.getFile().getFileName();
            profile.setProfilePic(event.getFile().getFileName().substring(0, newFile.length() - 4));
            oTUserEntitySessionBeanLocal.updateUserDetails(profile);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "File uploaded successfully", ""));
        } catch (IOException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload error: " + ex.getMessage(), ""));
        }
    }

    public void updateDetails(ActionEvent event) {
        System.out.println(">>>>>>>>Update details <<<<<");
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date updateDate = formatter.parse(profileDate);
            profile.setDob(updateDate);
            oTUserEntitySessionBeanLocal.updateUserDetails(profile);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Profile updated successfully", ""));

            PrimeFaces.current().executeScript("PF('dialogUpdateDetails').hide()");
        } catch (UpdateUserException | UserNotFoundException | InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Profile Update error: " + ex.getMessage(), ""));
        } catch (ParseException ex) {
            Logger.getLogger(ProfileManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void removeAddress(ActionEvent event) {
        try {
            Long addressId = (Long) event.getComponent().getAttributes().get("addressId");
            addressEntitySessionBeanLocal.removeAddress(addressId);
            addresses = addressEntitySessionBeanLocal.retrieveAddressesByUserId(profile.getUserId());
        } catch (NoAddressFoundException ex) {
            Logger.getLogger(ProfileManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void removeCreditCard(ActionEvent event) {
        try {
            Long creditCardId = (Long) event.getComponent().getAttributes().get("cardId");
            creditCardEntitySessionBeanLocal.deleteCreditCard(creditCardId);
            creditCards = creditCardEntitySessionBeanLocal.retrieveAllCardByUser(profile.getUserId());
        } catch (CreditCardNotFoundException ex) {
            Logger.getLogger(ProfileManagedBean.class.getName()).log(Level.SEVERE, null, ex); 
        }

    }

    public void addAddress(ActionEvent event) {
        try {
            addressEntitySessionBeanLocal.addAddressWithUserId(newAddress, profile.getUserId());
            addresses = addressEntitySessionBeanLocal.retrieveAddressesByUserId(profile.getUserId());
            region = null;
            newAddress = new AddressEntity();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Address Added successfully", ""));
            PrimeFaces.current().executeScript("PF('dialogAddAddress').hide()");
        } catch (UserNotFoundException | InputDataValidationException | UnknownPersistenceException | AddressExistException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Add Address error: " + ex.getMessage(), ""));
        } 
    }

    public void addCreditCard(ActionEvent event) {
        try {
            creditCardEntitySessionBeanLocal.createNewCreditCardForUser(newCreditCard, profile.getUserId());         
            newCreditCard = new CreditCardEntity();
            creditCards = creditCardEntitySessionBeanLocal.retrieveAllCardByUser(profile.getUserId());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Credit Card Added successfully", ""));
            PrimeFaces.current().executeScript("PF('dialogAddCreditCard').hide()");
        } catch (UnknownPersistenceException | CreditCardExistException | InputDataValidationException | UserNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Add Card error: " + ex.getMessage(), ""));
        } 
    }

    public void changePassword() {
        try {
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(oldPassword + profile.getSalt()));
            if (passwordHash.equals(profile.getPassword())) {
                if (newPassword.equals(confirmPassword)) {

                    oTUserEntitySessionBeanLocal.updatePassword(profile, oldPassword, newPassword);

                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Password changed successfully", null));
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "New password does not match", null));
                }
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Old password is incorrect", null));
            }

        } catch (InvalidLoginCredentialException | UserNotFoundException | InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating product: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    public void setRegion(AjaxBehaviorEvent event) {
        System.out.print(region);
        newAddress.setRegion(region);
        System.out.print(newAddress.getRegion());
    }

    public OTUserEntity getProfile() {
        return profile;
    }

    public void setProfile(OTUserEntity profile) {
        this.profile = profile;
    }

    public String getProfileDate() {
        return profileDate;
    }

    public void setProfileDate(String profileDate) {
        this.profileDate = profileDate;
    }

    public List<AddressEntity> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressEntity> addresses) {
        this.addresses = addresses;
    }

    public List<CreditCardEntity> getCreditCards() {
        return creditCards;
    }

    public void setCreditCards(List<CreditCardEntity> creditCards) {
        this.creditCards = creditCards;
    }

    public AddressEntity getNewAddress() {
        return newAddress;
    }

    public void setNewAddress(AddressEntity newAddress) {
        this.newAddress = newAddress;
    }

    public CreditCardEntity getNewCreditCard() {
        return newCreditCard;
    }

    public void setNewCreditCard(CreditCardEntity newCreditCard) {
        this.newCreditCard = newCreditCard;
    }

    public List<RegionEnum> getRegions() {
        return regions;
    }

    public void setRegions(List<RegionEnum> regions) {
        this.regions = regions;
    }

    public RegionEnum getRegion() {
        return region;
    }

    public void setRegion(RegionEnum region) {
        this.region = region;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

}
