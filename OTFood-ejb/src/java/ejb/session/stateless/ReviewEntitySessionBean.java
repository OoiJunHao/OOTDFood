/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AddressEntity;
import entity.MealEntity;
import entity.OTUserEntity;
import entity.ReviewEntity;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.DeleteReviewException;
import util.exception.InputDataValidationException;
import util.exception.NoReviewFoundException;
import util.exception.ReviewExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateReviewException;
import util.exception.UserExistException;
import util.exception.UserNotFoundException;

/**
 *
 * @author benny
 */
@Stateless
public class ReviewEntitySessionBean implements ReviewEntitySessionBeanLocal {

    @EJB(name = "OTUserEntitySessionBeanLocal")
    private OTUserEntitySessionBeanLocal oTUserEntitySessionBeanLocal;

    @PersistenceContext(unitName = "OTFood-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public ReviewEntitySessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Long addReview(ReviewEntity review, Long userId, Long mealId) throws ReviewExistException, UnknownPersistenceException, UserNotFoundException, InputDataValidationException {
        Set<ConstraintViolation<ReviewEntity>> constraintViolations = validator.validate(review);
        if (constraintViolations.isEmpty()) {
            try {
                OTUserEntity user = oTUserEntitySessionBeanLocal.retrieveUserById(userId);
                //MealEntity meal = add Meal retrieve by Id method MAKE NECESSARY CHANGES HERE!!
                MealEntity meal = new MealEntity(); //this is a stub
                user.getReviews().add(review);
                meal.getReviews().add(review);
                review.setUser(user);
                review.setMeal(meal);
                em.persist(review);
                em.flush();
                return review.getReviewId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new ReviewExistException("Review already Exists!");
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } catch (UserNotFoundException ex) {
                throw new UserNotFoundException(ex.getMessage());
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ReviewEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
    
    @Override
    public List<ReviewEntity> retrieveReviewsByUserId(Long userId) throws NoReviewFoundException {
        try {
            Query query = em.createQuery("SELECT r FROM OTUserEntity user JOIN user.reviews r WHERE user.UserId = :userId");
            query.setParameter("userId", userId);
            List<ReviewEntity> reviews = query.getResultList();
            return reviews;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new NoReviewFoundException("No Reviews are associcated with this userId: " + userId);
        }
    }
    
    @Override
    public List<ReviewEntity> retrieveReviewsByMealId(Long mealId) throws NoReviewFoundException {
        try {
            Query query = em.createQuery("SELECT r FROM MealEntity meal JOIN meal.reviews r WHERE meal.mealId = :mealId");
            query.setParameter("mealId", mealId);
            List<ReviewEntity> reviews = query.getResultList();
            return reviews;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new NoReviewFoundException("No Reviews are associated with this mealId: " + mealId);
        }
    }
    
    @Override
    public ReviewEntity retrieveReviewByUserId(Long userId, Long reviewId) throws NoReviewFoundException {
        try {
            Query query = em.createQuery("SELECT r FROM OTUserEntity user JOIN user.reviews r WHERE user.UserId = :userId AND r.reviewId = :reviewId");
            query.setParameter("userId", userId);
            query.setParameter("reviewId", reviewId);
            ReviewEntity review = (ReviewEntity) query.getSingleResult();
            return review;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new NoReviewFoundException("The reviewId: " + reviewId + " associcated with this userId: " + userId + " cannot be found!");
        }
    }
    
    @Override
    public ReviewEntity retrieveReviewByMealId(Long mealId, Long reviewId) throws NoReviewFoundException {
        try {
            Query query = em.createQuery("SELECT r FROM MealEntity meal JOIN meal.reviews r WHERE meal.mealId = :mealId AND r.reviewId = :reviewId");
            query.setParameter("mealId", mealId);
            query.setParameter("reviewId", reviewId);
            ReviewEntity review = (ReviewEntity) query.getSingleResult();
            return review;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new NoReviewFoundException("The reviewId: " + reviewId + " associcated with this userId: " + mealId + " cannot be found!");
        }
    }
    
    @Override
    public void deleteReviewByUserId(Long userId, Long reviewId) throws UserNotFoundException, NoReviewFoundException, DeleteReviewException {
        OTUserEntity user = oTUserEntitySessionBeanLocal.retrieveUserById(userId);
        ReviewEntity reviewToBeDeleted = retrieveReviewByUserId(userId, reviewId);
        if (user != null && reviewToBeDeleted != null) {
            user.getReviews().remove(reviewToBeDeleted);
            MealEntity meal = reviewToBeDeleted.getMeal();
            meal.getReviews().remove(reviewToBeDeleted);
            em.remove(reviewToBeDeleted);
        } else {
            throw new DeleteReviewException("either user or review cannot be detected!");
        }
    }
    
    @Override
    public void editReviewByUserId(Long userId, ReviewEntity review) throws NoReviewFoundException, UserNotFoundException, UpdateReviewException {
        OTUserEntity user = oTUserEntitySessionBeanLocal.retrieveUserById(userId);
        ReviewEntity currentReview = retrieveReviewByUserId(userId, review.getReviewId());
        if (currentReview != null) {
            currentReview.setDescription(review.getDescription());
            currentReview.setRating(review.getRating());
        } else {
            throw new UpdateReviewException("No review detected!");
        }
    }
    
    
}
