/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.DriverEntitySessionBeanLocal;
import ejb.session.stateless.StaffEntitySessionBeanLocal;
import entity.DriverEntity;
import entity.StaffEntity;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.exception.DriverAlreadyFoundException;
import util.exception.DriverNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.NoSaleTransactionFoundException;
import util.exception.UpdateDriverException;
import ws.datamodel.UpdateDriverReq;

/**
 * REST Web Service
 *
 * @author Mitsuki
 */
@Path("driverManagement")
public class DriverManagementResource {

    StaffEntitySessionBeanLocal staffEntitySessionBean;

    DriverEntitySessionBeanLocal driverEntitySessionBean;

    private final SessionBeanLookup sessionBeanLookUp;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of DriverManagementResource
     */
    public DriverManagementResource() {
        sessionBeanLookUp = new SessionBeanLookup();
        staffEntitySessionBean = sessionBeanLookUp.staffEntitySessionBean;
        driverEntitySessionBean = sessionBeanLookUp.driverEntitySessionBean;
    }

    @Path("retrieveAllDrivers")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllDrivers(@QueryParam("username") String username,
            @QueryParam("password") String password) {

        try {
            StaffEntity staff = staffEntitySessionBean.staffLogin(username, password);
            System.out.println("********** DriverManagement.retrieveAllDrivers(): Staff " + staff.getUsername() + " login remotely via web service");

            List<DriverEntity> drivers = driverEntitySessionBean.retrieveAllDrivers();
            for (DriverEntity driver: drivers) {
                driver.getSaleTransaction().clear();
            }

            GenericEntity<List<DriverEntity>> genericDriver = new GenericEntity<List<DriverEntity>>(drivers) {

            };
            return Response.status(Response.Status.OK).entity(genericDriver).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDriver(UpdateDriverReq updateDriverReq) {
        System.out.println(updateDriverReq.getDriver());
        if (updateDriverReq != null) {
            try {
                StaffEntity staff = staffEntitySessionBean.staffLogin(updateDriverReq.getUsername(), updateDriverReq.getPassword());
                System.out.println("********** DriverManagement.updateDriver(): Staff " + staff.getUsername() + " login remotely via web service");

                driverEntitySessionBean.updateDriver(updateDriverReq.getDriver());

                return Response.status(Response.Status.OK).build();
            } catch (InvalidLoginCredentialException ex) {
                return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (UpdateDriverException | InputDataValidationException | DriverNotFoundException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }

        } else {
            System.out.println("null driver");
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid update product request").build();
        }
    }

    @Path("{driverId}")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteDriver(@QueryParam("username") String username,
            @QueryParam("password") String password,
            @PathParam("driverId") Long driverId) {

        try {
            StaffEntity staffEntity = staffEntitySessionBean.staffLogin(username, password);
            System.out.println("********** DriverManagement.deleteDriver(): Staff " + staffEntity.getUsername() + " login remotely via web service");

            Boolean result = driverEntitySessionBean.setDriverActiveToFalse(driverId);
            return Response.status(Response.Status.OK).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (DriverNotFoundException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
    
    @Path("setSaleToDriver/{driverId}/{customerId}/{saleTransactionId}")
    @GET
    public Response updateDriverAndSale(@PathParam("driverId") long driverId, @PathParam("customerId")
            long customerId, @PathParam("saleTransactionId") long saleTransactionId) {
        try {
            driverEntitySessionBean.setDriverToSaleTransaction(driverId, customerId, saleTransactionId);
            return Response.status(Response.Status.OK).build();
        } catch (DriverNotFoundException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (NoSaleTransactionFoundException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (DriverAlreadyFoundException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
}
