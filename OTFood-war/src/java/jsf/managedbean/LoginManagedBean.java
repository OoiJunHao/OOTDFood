/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.OTUserEntitySessionBeanLocal;
import entity.OTUserEntity;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import util.exception.InvalidLoginCredentialException;
import util.exception.UserNotFoundException;

/**
 *
 * @author Ong Bik Jeun
 */
@Named(value = "loginManagedBean")
@RequestScoped
public class LoginManagedBean {

    @EJB
    private OTUserEntitySessionBeanLocal oTUserEntitySessionBean;
    
    @Inject
    private userManagementManagedBean userManagementManagedBean;
    
    private String email;
    private String password;

    /**
     * Creates a new instance of LoginManagedBean
     */
    public LoginManagedBean() {
    }

    public void login(ActionEvent event) throws IOException {
        System.out.println("TEST");
        try {
            OTUserEntity user = oTUserEntitySessionBean.login(email, password);
            FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("isLogin", true);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("currentUser", user);
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");
        } catch (InvalidLoginCredentialException | UserNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }
    }

    public void logout(ActionEvent event) throws IOException {
        System.out.println(">>>>>>>> LOGOUT <<<<<<<<<");
        ((HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true)).invalidate();
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public userManagementManagedBean getUserManagementManagedBean() {
        return userManagementManagedBean;
    }

    public void setUserManagementManagedBean(userManagementManagedBean userManagementManagedBean) {
        this.userManagementManagedBean = userManagementManagedBean;
    }
    
    

}
