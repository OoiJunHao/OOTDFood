/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.BentoEntity;
import entity.MealEntity;
import entity.OTUserEntity;
import java.util.ArrayList;
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
import util.exception.InputDataValidationException;
import util.exception.MealExistsException;
import util.exception.MealNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UserNotFoundException;

/**
 *
 * @author yuntiangu
 */
@Stateless
public class MealEntitySessionBean implements MealEntitySessionBeanLocal {

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
    public Long createNewMealForUser(Long userId, MealEntity meal) throws UnknownPersistenceException, InputDataValidationException, UserNotFoundException, MealExistsException {
        Set<ConstraintViolation<MealEntity>> constraintViolations = validator.validate(meal);
        if (constraintViolations.isEmpty()) {
            try {

                OTUserEntity user = OTUserEntitySessionBeanLocal.retrieveUserById(userId);
                em.persist(meal);
                user.getMeals().add(meal);
                meal.getUsers().add(user);
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
    public Long createNewMeal(MealEntity meal) {
        em.persist(meal);
        em.flush();
        return meal.getMealId();
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
    public List<MealEntity> retrieveAllMealsOrderedByUser(Long userId) throws UserNotFoundException {
        try {
            OTUserEntity user = OTUserEntitySessionBeanLocal.retrieveUserById(userId);
            user.getMeals().size();
            return user.getMeals();
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException("User Id " + userId + " does not exist!");
        }
    }

    @Override
    public List<MealEntity> retrieveAllMeals() {
        Query query = em.createQuery("SELECT m FROM MealEntity m");
        return query.getResultList();
    }

    @Override
    public List<MealEntity> retrieveMealsByCategory(String catName) {
        Query query = em.createQuery("SELECT m FROM MealEntity m, IN (m.categories) c WHERE :inCatName = c");
        query.setParameter("inCatName", catName);

        return query.getResultList();
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
    public List<MealEntity> retrieveTop5MealEntityByRating() {
        List<MealEntity> sortedList = sortMealEntityByRating();
        List<MealEntity> top5Meals = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            top5Meals.add(sortedList.get(i));
        }
        return top5Meals;
    }

}
