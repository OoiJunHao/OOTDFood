/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.SaleTransactionEntitySessionBeanLocal;
import entity.OTUserEntity;
import entity.SaleTransactionEntity;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Ooi Jun Hao
 */
@Named(value = "viewTransactionDetailManagedBean")
@ViewScoped
public class ViewTransactionDetailManagedBean implements Serializable {

    @EJB
    private SaleTransactionEntitySessionBeanLocal saleTransactionEntitySessionBeanLocal;

    private List<SaleTransactionEntity> allSaleTransactions;
    private SaleTransactionEntity selectedSaleTransaction;
    
    public ViewTransactionDetailManagedBean() {
        this.selectedSaleTransaction  = new SaleTransactionEntity();
        this.allSaleTransactions = new ArrayList<>();
    }
    
    @PostConstruct
    public void postConstruct() {
        OTUserEntity user = (OTUserEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser");
        this.allSaleTransactions = saleTransactionEntitySessionBeanLocal.retrieveSaleTransactionsByUserId(user.getUserId());      
        System.out.println("Calling vtmb");
        System.out.println(this.allSaleTransactions.get(2).getDeliveryStatus().toString());
        this.selectedSaleTransaction =this.allSaleTransactions.get(2);
    }
    
    public void redirectToInvoice(){
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("selectedID", this.selectedSaleTransaction.getSaleTransactionId());
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/profileFeature/transactionInvoice.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ViewTransactionDetailManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the selectedSaleTransaction
     */
    public SaleTransactionEntity getSelectedSaleTransaction() {
        return selectedSaleTransaction;
    }

    /**
     * @param selectedSaleTransaction the selectedSaleTransaction to set
     */
    public void setSelectedSaleTransaction(SaleTransactionEntity selectedSaleTransaction) {
        this.selectedSaleTransaction = selectedSaleTransaction;      
    }

    /**
     * @return the allSaleTransactions
     */
    public List<SaleTransactionEntity> getAllSaleTransactions() {
        return allSaleTransactions;
    }

    /**
     * @param allSaleTransactions the allSaleTransactions to set
     */
    public void setAllSaleTransactions(List<SaleTransactionEntity> allSaleTransactions) {
        this.allSaleTransactions = allSaleTransactions;
    }
    
}
