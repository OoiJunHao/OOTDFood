/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.SaleTransactionEntity;
import entity.SaleTransactionLineEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewSaleTransactionException;
import util.exception.InputDataValidationException;
import util.exception.NoSaleTransactionFoundException;
import util.exception.UpdateSaleTransactionException;

/**
 *
 * @author benny
 */
@Local
public interface SaleTransactionEntitySessionBeanLocal {

    public Long createNewSaleTransaction(Long userId, Long ccId, Long adressId, SaleTransactionEntity saleTransaction) throws CreateNewSaleTransactionException, InputDataValidationException;

    public List<SaleTransactionEntity> retrieveAllSaleTransaction() throws NoSaleTransactionFoundException;

    public List<SaleTransactionEntity> retrieveSaleTransactionsByUserId(Long userId);

    public SaleTransactionEntity retrieveSaleTransactionByUserId(Long userId, Long transactionId) throws NoSaleTransactionFoundException;

    public void updateSaleTransaction(Long userId, SaleTransactionEntity saleTransaction) throws UpdateSaleTransactionException;

    public Long createNewSaleTransactionWithPromo(Long userId, Long ccId, Long adressId, Long promoId, SaleTransactionEntity saleTransaction) throws CreateNewSaleTransactionException, InputDataValidationException;

    public List<SaleTransactionLineEntity> retrieveSaleTransactionLineItemsByMealId(Long mealId);

    public SaleTransactionEntity retrieveSaleTransactionById(Long saleTransactionId) throws NoSaleTransactionFoundException;
    
}
