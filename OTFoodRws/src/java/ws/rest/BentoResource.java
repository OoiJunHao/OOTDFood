/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.IngredientEntitySessionBeanLocal;
import ejb.session.stateless.MealEntitySessionBeanLocal;
import entity.BentoEntity;
import entity.IngredientEntity;
import entity.MealEntity;
import entity.OTUserEntity;
import entity.ReviewEntity;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import util.enumeration.CategoryEnum;
import util.exception.InputDataValidationException;
import util.exception.MealExistsException;
import util.exception.MealNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author benny
 */

@Path("Bento")
public class BentoResource {
    @Context
    private UriInfo context;
    
    private final SessionBeanLookup sessionBeanLookUp;
    private final MealEntitySessionBeanLocal mealEntitySessionBeanLocal;
    private final IngredientEntitySessionBeanLocal ingredientEntitySessionBeanLocal;
    
    public BentoResource() {
        sessionBeanLookUp = new SessionBeanLookup();
        mealEntitySessionBeanLocal = sessionBeanLookUp.mealEntitySessionBean;
        ingredientEntitySessionBeanLocal = sessionBeanLookUp.ingredientEntitySessionBean;
    }
    
    @Path("retrieveAllMeals")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllMeals() {
   
        List<MealEntity> allMeals = mealEntitySessionBeanLocal.retrieveAllMeals();

        for (MealEntity meal : allMeals) {
            for (ReviewEntity review : meal.getReviews()) {
                review.setMeal(null);
                review.setUser(null);
            }
        }
        
        GenericEntity<List<MealEntity>> genericEntity = new GenericEntity<List<MealEntity>>(allMeals) {};
        
        return Response.status(Status.OK).entity(genericEntity).build();
      
    }
    
    @Path("retrieveMeal/{mealId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveMeal(@PathParam("mealId") Long mealId) {
        try {
            MealEntity meal = mealEntitySessionBeanLocal.retrieveMealById(mealId);
            for (ReviewEntity review : meal.getReviews()) {
                review.setMeal(null);
                review.setUser(null);
            }
            return Response.status(Status.OK).entity(meal).build();
        } catch (MealNotFoundException ex) {
           return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMeal(BentoEntity meal) {
        if (meal != null) {
            try {
                Long newMealId = mealEntitySessionBeanLocal.createNewMeal(meal);
                return Response.status(Status.OK).entity(newMealId).build();
            } catch (InputDataValidationException | UnknownPersistenceException | MealExistsException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("cannot create meal entity that is null!").build();
        }
    }
    
    @Path("retrieveAllMealCategories")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllCategories() {
        List<CategoryEnum> categories = Arrays.asList(CategoryEnum.values());
        return Response.status(Status.OK).entity(categories).build();
    }
    
    @Path("retrieveAllIngredients")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllIngredients() {
        List<IngredientEntity> ingredients = ingredientEntitySessionBeanLocal.retrieveAllIngredients();
        return Response.status(Status.OK).entity(ingredients).build();
    }
    
}