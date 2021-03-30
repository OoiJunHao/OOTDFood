/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.StaffEntitySessionBeanLocal;
import entity.StaffEntity;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.InvalidLoginCredentialException;

/**
 * REST Web Service
 *
 * @author Mitsuki
 */
@Path("Staff")
public class StaffResource {

    StaffEntitySessionBeanLocal staffEntitySessionBean;

    private final SessionBeanLookup sessionBeanLookup;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of StaffResource
     */
    public StaffResource() {
        sessionBeanLookup = new SessionBeanLookup();
        staffEntitySessionBean = sessionBeanLookup.staffEntitySessionBean;
    }

    @Path("staffLogin")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response staffLogin(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        try {
            StaffEntity staffEntity = staffEntitySessionBean.staffLogin(username, password);
            System.out.println("********** StaffResource.staffLogin(): Staff " + staffEntity.getUsername() + " login remotely via web service");

            staffEntity.setPassword(null);
            staffEntity.setSalt(null);

            return Response.status(Status.OK).entity(staffEntity).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

}
