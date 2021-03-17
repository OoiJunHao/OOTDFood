/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.FaqSessionBeanLocal;
import ejb.session.stateless.MealEntitySessionBeanLocal;
import ejb.session.stateless.OTUserEntitySessionBeanLocal;
import ejb.session.stateless.ReviewEntitySessionBeanLocal;
import entity.BentoEntity;
import entity.FaqEntity;
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
import util.exception.FaqExistException;
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

    @EJB
    private FaqSessionBeanLocal faqSessionBean;

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
            MealEntity bento1 = new BentoEntity("bento1", BigDecimal.valueOf(8.00), "this is test bento 1", false, 450, "bento5.jpg");
            MealEntity bento2 = new BentoEntity("bento2", BigDecimal.valueOf(8.50), "this is test bento 2", false, 500, "bento5.jpg");
            MealEntity bento3 = new BentoEntity("bento3", BigDecimal.valueOf(9.00), "this is test bento 3", false, 550, "bento5.jpg");
            MealEntity bento4 = new BentoEntity("bento4", BigDecimal.valueOf(9.50), "this is test bento 4", false, 600, "bento5.jpg");
            MealEntity bento5 = new BentoEntity("bento5", BigDecimal.valueOf(10.00), "this is test bento 5", false, 650, "bento5.jpg");
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

            //Legit Faq DataInit
            FaqEntity faq1 = new FaqEntity("How can I track my order?", "You can track your order by viewing your sales transaction status. When being delivered a status marked 'DELIVERING' will appear.", "Orders");
            FaqEntity faq4 = new FaqEntity("I made a mistake. Can I change my order?", "We strive to process orders as quickly as possible. But we will try to accommodate any order change. The quickest way to get a hold of us is by emailing us at ootdFood@gmail.com.", "Orders");
            FaqEntity faq5 = new FaqEntity("Can i make custom orders", "Yes, of course! You can do so under our CYOB page under meals.", "Orders");
            FaqEntity faq6 = new FaqEntity("What payment methods do you accept?", "We accept Visa and Mastercard for online orders.", "Orders");

            FaqEntity faq2 = new FaqEntity("How long would it take to receieve my order?", "We have fixed delivery timings, lunch : 12-2pm, dinner: 6-8pm, supper : 12am-2am", "Delivery");
            FaqEntity faq3 = new FaqEntity("How much is delivery fee?", "We have a flat rate of $2 for regions within 5km from us and $5 for the rest", "Delivery");
            FaqEntity faq7 = new FaqEntity("How do i know my order is on the way?", "If the 'Ordered' status changes to 'Delivering', you can look forward to having your scrumptious bentos within 30mins.", "Delivery");

            FaqEntity faq11 = new FaqEntity("Where do we get our ingredients from?", "We source them from trustable suppliers providing only the best of the crops.", "Product");
            FaqEntity faq8 = new FaqEntity("Will my Bento look exactly like the photo?", "Our chefs guarantees individual attention to detail and quality assurance in every bowl of bento to leave the kitchen.", "Product");
            FaqEntity faq9 = new FaqEntity("Are there different sizes to the bento?", "Sorry but at the moment, we only offer a fixed protion size.", "Product");
            FaqEntity faq10 = new FaqEntity("Can I visit your shop?", "Currently we are a home grown business, hence we do not have an outlet store. We do appreciate your continuous support to allow us to achieve the dream of opening our own store", "Product");
            faqSessionBean.createNewFaq(faq1);
            faqSessionBean.createNewFaq(faq2);
            faqSessionBean.createNewFaq(faq3);
            faqSessionBean.createNewFaq(faq4);
            faqSessionBean.createNewFaq(faq5);
            faqSessionBean.createNewFaq(faq6);
            faqSessionBean.createNewFaq(faq7);
            faqSessionBean.createNewFaq(faq8);
            faqSessionBean.createNewFaq(faq9);
            faqSessionBean.createNewFaq(faq10);
            faqSessionBean.createNewFaq(faq11);

        } catch (UserExistException | UnknownPersistenceException | InputDataValidationException | ReviewExistException | UserNotFoundException | FaqExistException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
