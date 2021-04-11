/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import HelperClasses.SortByAvailability;
import entity.BentoEntity;
import entity.MealEntity;
import entity.OTUserEntity;
import entity.ReviewEntity;
import entity.SaleTransactionLineEntity;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.CategoryEnum;
import util.exception.InputDataValidationException;
import util.exception.MealExistsException;
import util.exception.MealNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author yuntiangu
 */
@Stateless
public class MealEntitySessionBean implements MealEntitySessionBeanLocal {

    @EJB(name = "SaleTransactionEntitySessionBeanLocal")
    private SaleTransactionEntitySessionBeanLocal saleTransactionEntitySessionBeanLocal;

    @EJB
    private OTUserEntitySessionBeanLocal OTUserEntitySessionBeanLocal;
    
    

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    @PersistenceContext
    private EntityManager em;

    public MealEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewMeal(MealEntity meal) throws InputDataValidationException, UnknownPersistenceException, MealExistsException {
        Set<ConstraintViolation<MealEntity>> constraintViolations = validator.validate(meal);
        if (constraintViolations.isEmpty()) {
            try {
                em.persist(meal);
                em.flush();
                return meal.getMealId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new MealExistsException();
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public MealEntity retrieveMealById(Long mealId) throws MealNotFoundException {
        MealEntity meal = em.find(MealEntity.class, mealId);
        if (meal != null) {
            return meal;
        } else {
            throw new MealNotFoundException("Meal Entity Id " + mealId + " does not exist!");
        }
    }

    @Override
    public List<MealEntity> retrieveAllMeals() {
        Query query = em.createQuery("SELECT m FROM MealEntity m");
        return query.getResultList();
    }
    
    
    @Override        
    public List<MealEntity> retrieveAllMealsSortedByAvailability() {
        Query query = em.createQuery("SELECT m FROM MealEntity m");
        List<MealEntity> mealEntities = query.getResultList();
        Collections.sort(mealEntities, new SortByAvailability());
        return mealEntities;
    }

    
    @Override
    public List<BentoEntity> retriveAllBentos() { 
        Query query = em.createQuery("SELECT b FROM BentoEntity b");
        return query.getResultList();
    }
    
    @Override
    public List<BentoEntity> retrieveBentosByCategory(String category) {
        
        //Query query = em.createQuery("SELECT b FROM BentoEntity b WHERE :inCategory MEMBER OF (b.categories)"); //This is not possible because categories is not an entity
        Query query = em.createQuery("SELECT b FROM BentoEntity b");
        
        List<BentoEntity> bentos = query.getResultList();
        List<BentoEntity> res = new ArrayList<>();
        for (BentoEntity b: bentos) {
            for (CategoryEnum c: b.getCategories()) {
                if (c.toString() == null ? category == null : c.toString().equals(category)) {
                    res.add(b);
                    break;
                }
            }
        }
        
        return res;
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<MealEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        return msg;
    }

    public List<MealEntity> sortMealEntityByRating() {
        Query query = em.createQuery("SELECT meals FROM MealEntity meals ORDER BY meals.averageRating DESC");
        return query.getResultList();
    }

    @Override
    public List<MealEntity> retrieveTop5MealEntityByRating() { // will just give 6 because of the layout of index.html
        List<MealEntity> sortedList = sortMealEntityByRating();
        List<MealEntity> top5Meals = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            top5Meals.add(sortedList.get(i));
        }
        return top5Meals;
    }
    
    @Override
    public void removeMeal(Long mealId) throws MealNotFoundException {
        MealEntity mealToBeDeleted = retrieveMealById(mealId);
        List<SaleTransactionLineEntity> saleTransactionLineItems = saleTransactionEntitySessionBeanLocal.retrieveSaleTransactionLineItemsByMealId(mealId);
        if (saleTransactionLineItems.isEmpty()) {
            for (ReviewEntity review : mealToBeDeleted.getReviews()) {
                OTUserEntity user = review.getUser();
                user.getReviews().remove(review);
                em.remove(review);
            }
            em.remove(mealToBeDeleted);
        } else {
            throw new MealNotFoundException("the meal to be deleted contains sale transaction line items associated with it!");
        }
    }
    @Override
    public void updateMeal(MealEntity meal) throws MealNotFoundException {
        MealEntity currentMealEntity = retrieveMealById(meal.getMealId());
        if (currentMealEntity != null) {
            currentMealEntity.setName(meal.getName());
            currentMealEntity.setDescription(meal.getDescription());
            currentMealEntity.setPrice(meal.getPrice());
            currentMealEntity.setCalorie(meal.getCalorie());
            currentMealEntity.setAverageRating(meal.getAverageRating());
            currentMealEntity.setCategories(meal.getCategories());
            currentMealEntity.setImage(meal.getImage());
            currentMealEntity.setIsAvailable(meal.isIsAvailable());
            currentMealEntity.setIngredients(meal.getIngredients());
        } else {
            throw new MealNotFoundException("The meal entered is null!");
        }
    }

}
