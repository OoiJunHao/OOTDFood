/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.SaleTransactionEntitySessionBeanLocal;
import entity.OTUserEntity;
import entity.SaleTransactionEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
