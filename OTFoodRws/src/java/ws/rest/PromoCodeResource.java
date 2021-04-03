/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.PromoSessionBeanLocal;
import ejb.session.stateless.StaffEntitySessionBeanLocal;
import entity.MealEntity;
import entity.PromoCodeEntity;
import entity.SaleTransactionEntity;
import entity.StaffEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.PromoCodeExistException;
import util.exception.PromotionNotFoundException;
import util.exception.UnknownPersistenceException;
import ws.datamodel.CreatePromoReq;
import ws.datamodel.UpdatePromoReq;

/**
 *
 * @author benny
 */
@Path("PromoCode")
public class PromoCodeResource {
    @Context
    private UriInfo context;
    
    private final SessionBeanLookup sessionBeanLookUp;
    private PromoSessionBeanLocal promoSessionBeanLocal;
    private StaffEntitySessionBeanLocal staffEntitySessionBeanLocal;
    public PromoCodeResource() {
        this.sessionBeanLookUp = new SessionBeanLookup();
        this.promoSessionBeanLocal = sessionBeanLookUp.promoSessionBean;
        this.staffEntitySessionBeanLocal = sessionBeanLookUp.staffEntitySessionBean;
    }
    
    @Path("retrieveAllPromoCode")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllPromoCodes(@QueryParam("username") String username, @QueryParam("password") String password) {
        try {
           StaffEntity staffEntity = staffEntitySessionBeanLocal.staffLogin(username, password);
           List<PromoCodeEntity> listOfPromoCodes = promoSessionBeanLocal.retreieveAllPromoCode();
           for (PromoCodeEntity promoCode : listOfPromoCodes) {
               List<SaleTransactionEntity> saleTransactions = promoCode.getSaleTransaction();
               for (SaleTransactionEntity saleTransaction : saleTransactions) {
                   saleTransaction.setSaleTransactionLineItemEntities(null);
                   saleTransaction.setPromoCode(null);
                   saleTransaction.setDriver(null);
                   saleTransaction.setAddress(null);
                   saleTransaction.setCreditCardEntity(null);
               }
           }
           GenericEntity<List<PromoCodeEntity>> genericEntity = new GenericEntity<List<PromoCodeEntity>>(listOfPromoCodes){};
            return Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } 
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewPromoCode(CreatePromoReq createPromoReq) {
        try {
            StaffEntity staffEntity = staffEntitySessionBeanLocal.staffLogin(createPromoReq.getUsername(), createPromoReq.getPassword());
            PromoCodeEntity newPromoCode = promoSessionBeanLocal.createNewPromoCode(createPromoReq.getPromoCodeEntity());
            return Response.status(Response.Status.OK).entity(newPromoCode.getPromoCodeId()).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (UnknownPersistenceException | PromoCodeExistException | InputDataValidationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePromoCode(UpdatePromoReq updatePromoReq) {
        try {
            StaffEntity staffEntity = staffEntitySessionBeanLocal.staffLogin(updatePromoReq.getUsername(), updatePromoReq.getPassword());
            promoSessionBeanLocal.updateCode(updatePromoReq.getPromoEntity());
            return Response.status(Response.Status.OK).entity(updatePromoReq.getPromoEntity().getPromoCodeId()).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (PromotionNotFoundException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }
}


