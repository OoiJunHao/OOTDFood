/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import HelperClasses.RankingMeal;
import ejb.session.stateless.MealEntitySessionBeanLocal;
import ejb.session.stateless.ReviewEntitySessionBeanLocal;
import entity.MealEntity;
import entity.ReviewEntity;
import java.util.ArrayList;
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
    private List<RankingMeal> mealsWithRank;
   
    
    
    /**
     * Creates a new instance of IndexManagedBean
     */
    public IndexManagedBean() {
        mealsWithRank = new ArrayList<>();
    }
    
    @PostConstruct
    public void postConstruct() {
        top5Meals = mealEntitySessionBeanLocal.retrieveTop5MealEntityByRating();
        top2ReviewsForTop5Meals = reviewEntitySessionBeanLocal.top2ReviewsForTop5Meals();
        for (int i = 0; i < top5Meals.size(); i++) {
            mealsWithRank.add(new RankingMeal(top5Meals.get(i), i+1));
        }
        
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

    public List<RankingMeal> getMealsWithRank() {
        return mealsWithRank;
    }

    public void setMealsWithRank(List<RankingMeal> mealsWithRank) {
        this.mealsWithRank = mealsWithRank;
    }

    
    
    
    
    
    
}