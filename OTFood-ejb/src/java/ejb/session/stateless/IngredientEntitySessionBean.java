/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.IngredientEntity;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.IngredientDeductException;
import util.exception.IngredientNotFoundException;

/**
 *
 * @author benny
 */
@Stateless
public class IngredientEntitySessionBean implements IngredientEntitySessionBeanLocal {

    @PersistenceContext(unitName = "OTFood-ejbPU")
    private EntityManager em;

    public IngredientEntity retrieveIngredientById(Long ingredientId) throws IngredientNotFoundException {
        try {
            Query query = em.createQuery("SELECT i FROM IngredientEntity i WHERE i.ingredientId = :ingredientId");
            query.setParameter("ingredientId", ingredientId);
            IngredientEntity retrievedIngredient = (IngredientEntity) query.getSingleResult();
            return retrievedIngredient;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new IngredientNotFoundException("ingredient Id: " + ingredientId + " has no ingredient!");
        }
    }
    
    public void deductStockQuantity(Long ingredientId, Integer amountToDeduct) throws IngredientDeductException {
        try {
            IngredientEntity ingredient = retrieveIngredientById(ingredientId);
            if (amountToDeduct <= ingredient.getStockQuantity()) {
                Integer amountLeft = ingredient.getStockQuantity() - amountToDeduct;
                ingredient.setStockQuantity(amountLeft);
            } else {
                throw new IngredientDeductException("The amount to deduct is more than the current stock quantity!");
            }
        } catch (IngredientNotFoundException ex) {
            throw new IngredientDeductException("Ingredient provided cannot be found!");
        }
    }

    
}
