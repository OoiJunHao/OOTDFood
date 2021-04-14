/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 *
 * @author Ong Bik Jeun
 */
@Named(value = "footerManagedBean")
@RequestScoped
public class FooterManagedBean {

    /**
     * Creates a new instance of FooterManagedBean
     */
    public FooterManagedBean() {
    }

    public void redirectToFAQ(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/userPages/faq.xhtml");
    }

}
