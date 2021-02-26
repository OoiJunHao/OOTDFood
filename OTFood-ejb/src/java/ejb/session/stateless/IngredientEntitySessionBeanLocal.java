/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.IngredientEntity;
import javax.ejb.Local;
import util.exception.IngredientDeductException;
import util.exception.IngredientNotFoundException;

/**
 *
 * @author benny
 */
@Local
public interface IngredientEntitySessionBeanLocal {

    public IngredientEntity retrieveIngredientById(Long ingredientId) throws IngredientNotFoundException;

    public void deductStockQuantity(Long ingredientId, Integer amountToDeduct) throws IngredientDeductException;
    
}
