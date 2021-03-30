/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.OTUserEntitySessionBeanLocal;
import entity.OTUserEntity;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * REST Web Service
 *
 * @author yuntiangu
 */
@Path("OTUser")
public class OTUserResource {

    OTUserEntitySessionBeanLocal oTUserEntitySessionBean = lookupOTUserEntitySessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of OTUserResource
     */
    public OTUserResource() {
    }

    @Path("retrieveAllOTUsers")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllOTUsers() {
        System.out.println("~~~~~~~~~Retrieving all users......");
        try {
            List<OTUserEntity> allUsers = oTUserEntitySessionBean.retrieveAllUser();
            for (OTUserEntity user: allUsers) {
                System.out.println(user.getFirstname());
                user.getAddress().clear();
                user.getCreditCard().clear();
                user.getSaleTransaction().clear();
                user.getReviews().clear();
            }
            System.out.println("All Users Lists from rws: " + allUsers.toString());
            GenericEntity<List<OTUserEntity>> genericEntity = new GenericEntity<List<OTUserEntity>>(allUsers) {
            };
            return Response.status(Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    

    private OTUserEntitySessionBeanLocal lookupOTUserEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (OTUserEntitySessionBeanLocal) c.lookup("java:global/OTFood/OTFood-ejb/OTUserEntitySessionBean!ejb.session.stateless.OTUserEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
