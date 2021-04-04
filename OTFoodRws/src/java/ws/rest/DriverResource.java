/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.DriverEntitySessionBeanLocal;
import entity.DriverEntity;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.exception.DriverExistsException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import ws.datamodel.CreateDriverReq;

/**
 * REST Web Service
 *
 * @author Mitsuki
 */
@Path("Driver")
public class DriverResource {

    DriverEntitySessionBeanLocal driverEntitySessionBean;
    private final SessionBeanLookup sessionBeanLookup;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of DriverResource
     */
    public DriverResource() {
        sessionBeanLookup = new SessionBeanLookup();
        driverEntitySessionBean = sessionBeanLookup.driverEntitySessionBean;
    }

    @Path("driverLogin")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response driverLogin(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        try {
            DriverEntity driver = driverEntitySessionBean.driverLogin(username, password);

            driver.setPassword(null);
            driver.setSalt(null);

            return Response.status(Response.Status.OK).entity(driver).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createDriver(CreateDriverReq createDriverReq) {

        if (createDriverReq != null) {
            try {
                Long driverId = driverEntitySessionBean.createNewDriver(createDriverReq.getNewDriver());
                return Response.status(Response.Status.OK).entity(driverId).build();
            } catch (UnknownPersistenceException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (InputDataValidationException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (DriverExistsException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new staff request").build();
        }
    }
}
