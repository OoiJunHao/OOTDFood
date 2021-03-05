/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.MealEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.InputDataValidationException;
import util.exception.MealExistsException;
import util.exception.MealNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UserNotFoundException;

/**
 *
 * @author yuntiangu
 */
@Local
public interface MealEntitySessionBeanLocal {

    public List<MealEntity> retrieveAllMeals();

    public List<MealEntity> retrieveAllMealsOrderedByUser(Long userId) throws UserNotFoundException;

    public MealEntity retrieveMealById(Long mealId) throws MealNotFoundException;

    public Long createNewMealForUser(Long userId, MealEntity meal) throws UnknownPersistenceException, InputDataValidationException, UserNotFoundException, MealExistsException;

    public List<MealEntity> retrieveMealsByCategory(String catName);
    
}
