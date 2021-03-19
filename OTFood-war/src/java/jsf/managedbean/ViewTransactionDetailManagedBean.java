/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.SaleTransactionEntity;
import java.io.Serializable;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Ooi Jun Hao
 */
@Named(value = "viewTransactionDetailManagedBean")
@ViewScoped
public class ViewTransactionDetailManagedBean implements Serializable {

    private SaleTransactionEntity selectedSaleTransaction;
    
    public ViewTransactionDetailManagedBean() {
        this.selectedSaleTransaction  = new SaleTransactionEntity();
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
    
}
