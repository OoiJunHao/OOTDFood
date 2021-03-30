/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.ReviewEntitySessionBeanLocal;
import ejb.session.stateless.StaffEntitySessionBeanLocal;
import entity.ReviewEntity;
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
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.exception.InvalidLoginCredentialException;

/**
 * REST Web Service
 *
 * @author Mitsuki
 */
@Path("reviewManagement")
public class ReviewManagementResource {

    ReviewEntitySessionBeanLocal reviewEntitySessionBean;
    private final StaffEntitySessionBeanLocal staffEntitySessionBeanLocal;
    private final SessionBeanLookup sessionBeanLookUp;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ReviewManagementResource
     */
    public ReviewManagementResource() {
        sessionBeanLookUp = new SessionBeanLookup();
        reviewEntitySessionBean = sessionBeanLookUp.reviewEntitySessionBean;
        staffEntitySessionBeanLocal = sessionBeanLookUp.staffEntitySessionBean;
    }

    @Path("retrieveAllReviews")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllReviews(@QueryParam("username") String username,
            @QueryParam("password") String password) {

        try {
            StaffEntity staff = staffEntitySessionBeanLocal.staffLogin(username, password);
            System.out.println("********** StaffManagement.retrieveAllStaff(): Staff " + staff.getUsername() + " login remotely via web service");

            List<ReviewEntity> reviews = reviewEntitySessionBean.retrieveAllReviews();
            GenericEntity<List<ReviewEntity>> genericReview = new GenericEntity<List<ReviewEntity>>(reviews) {

            };

            return Response.status(Response.Status.OK).entity(genericReview).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }

    }

}
