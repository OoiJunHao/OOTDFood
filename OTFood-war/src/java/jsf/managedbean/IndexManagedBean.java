/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.MealEntitySessionBeanLocal;
import ejb.session.stateless.ReviewEntitySessionBeanLocal;
import entity.MealEntity;
import entity.ReviewEntity;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author benny
 */
@Named(value = "indexManagedBean")
@RequestScoped
public class IndexManagedBean {

    @EJB(name = "ReviewEntitySessionBeanLocal")
    private ReviewEntitySessionBeanLocal reviewEntitySessionBeanLocal;

    @EJB(name = "MealEntitySessionBeanLocal")
    private MealEntitySessionBeanLocal mealEntitySessionBeanLocal;
    
    
    
    private List<MealEntity> top5Meals;
    private List<ReviewEntity> top2ReviewsForTop5Meals;
   
    
    
    /**
     * Creates a new instance of IndexManagedBean
     */
    public IndexManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct() {
        top5Meals = mealEntitySessionBeanLocal.retrieveTop5MealEntityByRating();
        top2ReviewsForTop5Meals = reviewEntitySessionBeanLocal.top2ReviewsForTop5Meals();
    }

    public List<MealEntity> getTop5Meals() {
        return top5Meals;
    }

    public void setTop5Meals(List<MealEntity> top5Meals) {
        this.top5Meals = top5Meals;
    }

    public List<ReviewEntity> getTop2ReviewsForTop5Meals() {
        return top2ReviewsForTop5Meals;
    }

    public void setTop2ReviewsForTop5Meals(List<ReviewEntity> top2ReviewsForTop5Meals) {
        this.top2ReviewsForTop5Meals = top2ReviewsForTop5Meals;
    }

    
    
    
    
    
    
}
