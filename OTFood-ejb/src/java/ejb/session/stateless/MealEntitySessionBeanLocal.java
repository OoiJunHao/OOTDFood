/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.BentoEntity;
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

    public MealEntity retrieveMealById(Long mealId) throws MealNotFoundException;

   public Long createNewMeal(MealEntity meal) throws InputDataValidationException, UnknownPersistenceException, MealExistsException;

    public List<MealEntity> retrieveTop5MealEntityByRating();

    public List<BentoEntity> retriveAllBentos();

    public List<BentoEntity> retrieveBentosByCategory(String category);

    public void removeMeal(Long mealId) throws MealNotFoundException;

    public void updateMeal(MealEntity meal) throws MealNotFoundException;

    public List<MealEntity> retrieveAllMealsSortedByAvailability();

}
