/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.MealEntitySessionBeanLocal;
import ejb.session.stateless.OTUserEntitySessionBeanLocal;
import ejb.session.stateless.ReviewEntitySessionBeanLocal;
import entity.BentoEntity;
import entity.MealEntity;
import entity.OTUserEntity;
import entity.ReviewEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.InputDataValidationException;
import util.exception.ReviewExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UserExistException;
import util.exception.UserNotFoundException;

/**
 *
 * @author benny
 */
@Startup
@Singleton
@LocalBean
public class DataInitializationSessionBean {

    @EJB(name = "ReviewEntitySessionBeanLocal")
    private ReviewEntitySessionBeanLocal reviewEntitySessionBeanLocal;

    @EJB(name = "OTUserEntitySessionBeanLocal")
    private OTUserEntitySessionBeanLocal oTUserEntitySessionBeanLocal;

    @EJB(name = "MealEntitySessionBeanLocal")
    private MealEntitySessionBeanLocal mealEntitySessionBeanLocal;

    @PersistenceContext(unitName = "OTFood-ejbPU")
    private EntityManager em;

    @PostConstruct
    public void postConstruct() {
        if (em.find(MealEntity.class, 1l) == null) {
            dataInitialise();
        }
    }

    public void persist(Object object) {
        em.persist(object);
    }

    private void dataInitialise() {
        try {
            OTUserEntity user = new OTUserEntity("bennyphoe1998@gmail.com", "test", 90909090l, "test", "test", new Date(), "test");
            Long customerId = oTUserEntitySessionBeanLocal.createNewUser(user);
            List<MealEntity> bentoSets = new ArrayList<>();
            MealEntity bento1 = new BentoEntity("bento1", BigDecimal.valueOf(8.00), "this is test bento 1", false, 450);
            MealEntity bento2 = new BentoEntity("bento2", BigDecimal.valueOf(8.50), "this is test bento 2", false, 500);
            MealEntity bento3 = new BentoEntity("bento3", BigDecimal.valueOf(9.00), "this is test bento 3", false, 550);
            MealEntity bento4 = new BentoEntity("bento4", BigDecimal.valueOf(9.50), "this is test bento 4", false, 600);
            MealEntity bento5 = new BentoEntity("bento5", BigDecimal.valueOf(10.00), "this is test bento 5", false, 650);
            bentoSets.add(bento1);
            bentoSets.add(bento2);
            bentoSets.add(bento3);
            bentoSets.add(bento4);
            bentoSets.add(bento5);
            for (MealEntity mealEntity : bentoSets) {
                mealEntitySessionBeanLocal.createNewMeal(mealEntity);
            }
            ReviewEntity review1 = new ReviewEntity(5, "This is amazing!", new Date());
            ReviewEntity review2 = new ReviewEntity(5, "This is amazing!", new Date());
            ReviewEntity review3 = new ReviewEntity(5, "This is amazing!", new Date());
            ReviewEntity review4 = new ReviewEntity(5, "This is amazing!", new Date());
            ReviewEntity review5 = new ReviewEntity(5, "This is amazing!", new Date());
            ReviewEntity review6 = new ReviewEntity(5, "This is amazing!", new Date());
            ReviewEntity review7 = new ReviewEntity(5, "This is amazing!", new Date());
            ReviewEntity review8 = new ReviewEntity(5, "This is amazing!", new Date());
            ReviewEntity review9 = new ReviewEntity(5, "This is amazing!", new Date());
            ReviewEntity review10 = new ReviewEntity(5, "This is amazing!", new Date());
            reviewEntitySessionBeanLocal.addReview(review1, customerId, 1l);
            reviewEntitySessionBeanLocal.addReview(review2, customerId, 1l);
            reviewEntitySessionBeanLocal.addReview(review3, customerId, 2l);
            reviewEntitySessionBeanLocal.addReview(review4, customerId, 2l);
            reviewEntitySessionBeanLocal.addReview(review5, customerId, 3l);
            reviewEntitySessionBeanLocal.addReview(review6, customerId, 3l);
            reviewEntitySessionBeanLocal.addReview(review7, customerId, 4l);
            reviewEntitySessionBeanLocal.addReview(review8, customerId, 4l);
            reviewEntitySessionBeanLocal.addReview(review9, customerId, 5l);
            reviewEntitySessionBeanLocal.addReview(review10, customerId, 5l);
        } catch (UserExistException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownPersistenceException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InputDataValidationException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ReviewExistException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserNotFoundException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
