/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.OTUserEntitySessionBeanLocal;
import entity.AddressEntity;
import entity.CreditCardEntity;
import entity.IngredientEntity;
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
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import util.enumeration.RegionEnum;
import util.exception.InputDataValidationException;
import util.exception.UpdateUserException;
import util.exception.UserNotFoundException;

/**
 *
 * @author benny
 */
@Named(value = "profileManagedBean")
@ViewScoped
public class ProfileManagedBean implements Serializable {

    @EJB(name = "OTUserEntitySessionBeanLocal")
    private OTUserEntitySessionBeanLocal oTUserEntitySessionBeanLocal;

    /**
     * Creates a new instance of ProfileManagedBean
     */
    private OTUserEntity profile;
    private String profileDate;
    private List<AddressEntity> addresses;
    private List<CreditCardEntity> creditCards;
    private AddressEntity newAddress;
    private CreditCardEntity newCreditCard;
    private List<RegionEnum> regions;
    private RegionEnum region;

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
        addresses = profile.getAddress();
        creditCards = profile.getCreditCard();
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
            for (int i = 0; i < addresses.size(); i++) {
                if (addresses.get(i).getAddressId() == addressId) {
                    addresses.remove(i);
                    break;
                }
            }
            profile.setAddress(addresses);
            oTUserEntitySessionBeanLocal.updateUserDetails(profile);
        } catch (UpdateUserException ex) {
            Logger.getLogger(ProfileManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserNotFoundException ex) {
            Logger.getLogger(ProfileManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InputDataValidationException ex) {
            Logger.getLogger(ProfileManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void removeCreditCard(ActionEvent event) {
        try {
            Long creditCardId = (Long) event.getComponent().getAttributes().get("cardId");
            for (int i = 0; i < creditCards.size(); i++) {
                if (creditCards.get(i).getCreditCardId() == creditCardId) {
                    creditCards.remove(i);
                    break;
                }
            }
            profile.setCreditCard(creditCards);
            oTUserEntitySessionBeanLocal.updateUserDetails(profile);
        } catch (UpdateUserException ex) {
            Logger.getLogger(ProfileManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserNotFoundException ex) {
            Logger.getLogger(ProfileManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InputDataValidationException ex) {
            Logger.getLogger(ProfileManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void addAddress(ActionEvent event) {
        try {
            addresses.add(newAddress);
            profile.setAddress(addresses);
            oTUserEntitySessionBeanLocal.updateUserDetails(profile);
            region = null;
            newAddress = new AddressEntity();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Address Added successfully", ""));

        } catch (UpdateUserException | UserNotFoundException | InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Add Address error: " + ex.getMessage(), ""));
        }
    }

    public void addCreditCard(ActionEvent event) {
        try {
            creditCards.add(newCreditCard);
            profile.setCreditCard(creditCards);
            oTUserEntitySessionBeanLocal.updateUserDetails(profile);
            newCreditCard = new CreditCardEntity();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Credit Card Added successfully", ""));

        } catch (UpdateUserException | UserNotFoundException | InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Add Card error: " + ex.getMessage(), ""));
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

}
