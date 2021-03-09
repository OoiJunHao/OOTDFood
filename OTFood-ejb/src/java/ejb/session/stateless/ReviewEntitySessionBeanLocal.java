/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.ReviewEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.DeleteReviewException;
import util.exception.InputDataValidationException;
import util.exception.NoReviewFoundException;
import util.exception.ReviewExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateReviewException;
import util.exception.UserNotFoundException;

/**
 *
 * @author benny
 */
@Local
public interface ReviewEntitySessionBeanLocal {

    public Long addReview(ReviewEntity review, Long userId, Long mealId) throws ReviewExistException, UnknownPersistenceException, UserNotFoundException, InputDataValidationException;

    public List<ReviewEntity> retrieveReviewsByUserId(Long userId) throws NoReviewFoundException;

    public List<ReviewEntity> retrieveReviewsByMealId(Long mealId) throws NoReviewFoundException;

    public ReviewEntity retrieveReviewByUserId(Long userId, Long reviewId) throws NoReviewFoundException;

    public ReviewEntity retrieveReviewByMealId(Long mealId, Long reviewId) throws NoReviewFoundException;

    public void deleteReviewByUserId(Long userId, Long reviewId) throws UserNotFoundException, NoReviewFoundException, DeleteReviewException;

    public void editReviewByUserId(Long userId, ReviewEntity review) throws NoReviewFoundException, UserNotFoundException, UpdateReviewException;

    public List<ReviewEntity> top2ReviewsForTop5Meals();
    
}
