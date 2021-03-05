/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.IngredientEntity;
<<<<<<< HEAD
import javax.ejb.Local;
import util.exception.IngredientDeductException;
import util.exception.IngredientNotFoundException;

/**
 *
 * @author benny
=======
import java.util.List;
import javax.ejb.Local;
import util.exception.IngredientEntityExistsException;
import util.exception.IngredientEntityNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author yuntiangu
>>>>>>> 44c7077cccc1ebd5ba974ba79e155a08c06f836c
 */
@Local
public interface IngredientEntitySessionBeanLocal {

<<<<<<< HEAD
    public IngredientEntity retrieveIngredientById(Long ingredientId) throws IngredientNotFoundException;

    public void deductStockQuantity(Long ingredientId, Integer amountToDeduct) throws IngredientDeductException;
=======
    public List<IngredientEntity> retrieveAllIngredients();

    public List<IngredientEntity> retrieveIngredientsWithMatchingName(String inputName);

    public IngredientEntity retrieveIngredientById(Long ingreId) throws IngredientEntityNotFoundException;

    public Long createIngredientEntityForMeal(IngredientEntity ingre) throws IngredientEntityExistsException, UnknownPersistenceException, InputDataValidationException;
>>>>>>> 44c7077cccc1ebd5ba974ba79e155a08c06f836c
    
}
