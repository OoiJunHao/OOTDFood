/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.IngredientEntitySessionBeanLocal;
import ejb.session.stateless.StaffEntitySessionBeanLocal;
import entity.IngredientEntity;
import entity.PromoCodeEntity;
import entity.StaffEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.exception.IngredientEntityExistsException;
import util.exception.IngredientEntityNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateIngredientException;
import ws.datamodel.CreateIngredientReq;
import ws.datamodel.UpdateIngredientReq;

/**
 * REST Web Service
 *
 * @author Mitsuki
 */
@Path("ingredientManagement")
public class IngredientManagementResource {

    StaffEntitySessionBeanLocal staffEntitySessionBean;

    IngredientEntitySessionBeanLocal ingredientEntitySessionBean;

    private SessionBeanLookup sessionBeanLookUp;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of IngredientManagementResource
     */
    public IngredientManagementResource() {
        sessionBeanLookUp = new SessionBeanLookup();
        staffEntitySessionBean = sessionBeanLookUp.staffEntitySessionBean;
        ingredientEntitySessionBean = sessionBeanLookUp.ingredientEntitySessionBean;
    }

    @Path("retrieveAllIngredients")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllIngredients(@QueryParam("username") String username, @QueryParam("password") String password) {
        try {

            StaffEntity staffEntity = staffEntitySessionBean.staffLogin(username, password);
            List<IngredientEntity> ingredients = ingredientEntitySessionBean.retrieveAllIngredients();
            GenericEntity<List<IngredientEntity>> genericEntity = new GenericEntity<List<IngredientEntity>>(ingredients) {
            };
            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewIngredient(CreateIngredientReq createIngredientReq) {
        try {
            StaffEntity staffEntity = staffEntitySessionBean.staffLogin(createIngredientReq.getUsername(), createIngredientReq.getPassword());
            Long ingredId = ingredientEntitySessionBean.createIngredientEntityForMeal(createIngredientReq.getIngredient());
            IngredientEntity ingred = ingredientEntitySessionBean.retrieveIngredientById(ingredId);
            return Response.status(Response.Status.OK).entity(ingred).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (IngredientEntityExistsException | UnknownPersistenceException | InputDataValidationException | IngredientEntityNotFoundException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateIngredient(UpdateIngredientReq updateIngredientReq) {
        try {
            StaffEntity staffEntity = staffEntitySessionBean.staffLogin(updateIngredientReq.getUsername(), updateIngredientReq.getPassword());
            System.out.println(updateIngredientReq.getIngredient());
            ingredientEntitySessionBean.updateIngredient(updateIngredientReq.getIngredient());
            System.out.println(updateIngredientReq.getIngredient());
            IngredientEntity updatedIngred = ingredientEntitySessionBean.retrieveIngredientById(updateIngredientReq.getIngredient().getIngredientId());
            return Response.status(Response.Status.OK).entity(updatedIngred).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (InputDataValidationException | IngredientEntityNotFoundException | UpdateIngredientException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }

    }

}
