/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.MealEntitySessionBeanLocal;
import entity.MealEntity;
import entity.OTUserEntity;
import entity.ReviewEntity;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

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
    
    public BentoResource() {
        sessionBeanLookUp = new SessionBeanLookup();
        mealEntitySessionBeanLocal = sessionBeanLookUp.mealEntitySessionBean;
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
}