/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.StaffEntitySessionBeanLocal;
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
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.StaffNotFoundException;
import util.exception.StaffUsernameExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateStaffException;
import ws.datamodel.CreateStaffReq;
import ws.datamodel.UpdateStaffReq;

/**
 * REST Web Service
 *
 * @author Mitsuki
 */
@Path("staffManagement")
public class StaffManagementResource {

    StaffEntitySessionBeanLocal staffEntitySessionBean;
    private final SessionBeanLookup sessionBeanLookUp;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of StaffManagementResource
     */
    public StaffManagementResource() {
        sessionBeanLookUp = new SessionBeanLookup();
        staffEntitySessionBean = sessionBeanLookUp.staffEntitySessionBean;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createStaff(CreateStaffReq createStaffReq) {
        if (createStaffReq != null) {
            try {
//                StaffEntity staff = staffEntitySessionBean.staffLogin(createStaffReq.getUsername(), createStaffReq.getPassword());
//                System.out.println("********** StaffManagement.retrieveAllStaff(): Staff " + staff.getUsername() + " login remotely via web service");

                Long staffId = staffEntitySessionBean.createNewStaff(createStaffReq.getStaff());

                return Response.status(Response.Status.OK).entity(staffId).build();
//            } catch (InvalidLoginCredentialException ex) {
//                return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (StaffUsernameExistException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (UnknownPersistenceException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (InputDataValidationException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new staff request").build();
        }
    }

    @Path("retrieveAllStaffs")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllStaffs(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        try {
//            StaffEntity staff = staffEntitySessionBean.staffLogin(username, password);
//            System.out.println("********** StaffManagement.retrieveAllStaff(): Staff " + staff.getUsername() + " login remotely via web service");

            List<StaffEntity> staffs = staffEntitySessionBean.retrieveAllStaff();
            GenericEntity<List<StaffEntity>> genericStaff = new GenericEntity<List<StaffEntity>>(staffs) {

            };
            return Response.status(Status.OK).entity(genericStaff).build();
//        } catch (InvalidLoginCredentialException ex) {
//            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }

    }

    @Path("retrieveStaff/{staffId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveStaff(@QueryParam("username") String username,
            @QueryParam("password") String password,
            @PathParam("staffId") Long staffId) {
        try {
            StaffEntity staff = staffEntitySessionBean.staffLogin(username, password);
            System.out.println("********** StaffManagementResource.retrieveStaff(): Staff " + staff.getUsername() + " login remotely via web service");

            StaffEntity requiredStaff = staffEntitySessionBean.retrieveStaffById(staffId);

            return Response.status(Status.OK).entity(requiredStaff).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (StaffNotFoundException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStaff(UpdateStaffReq updateStaffReq) {
        System.out.println(">>>>>>>> UPDATE STAFF <<<<<");
        System.out.println(updateStaffReq.getStaff());
        if (updateStaffReq != null) {
            try {
//                StaffEntity staff = staffEntitySessionBean.staffLogin(updateStaffReq.getUsername(), updateStaffReq.getPassword());
//                System.out.println("********** StaffManagementResource.updateStaff(): Staff " + staff.getUsername() + " login remotely via web service");

                staffEntitySessionBean.updateStaff(updateStaffReq.getStaff());

                return Response.status(Response.Status.OK).build();
//            } catch (InvalidLoginCredentialException ex) {
//                return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
            } catch (StaffNotFoundException | UpdateStaffException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            } catch (Exception ex) {
                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
            }
        } else {
            System.out.println("null staff");
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid update product request").build();
        }
    }

    @Path("{staffId}")
    @DELETE
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProduct(@QueryParam("username") String username,
            @QueryParam("password") String password,
            @PathParam("staffId") Long staffId) {
        try {
//            StaffEntity staffEntity = staffEntitySessionBean.staffLogin(username, password);
//            System.out.println("********** StaffResource.deleteStaff(): Staff " + staffEntity.getUsername() + " login remotely via web service");

            staffEntitySessionBean.deleteStaff(staffId);
            return Response.status(Status.OK).build();
        } catch (StaffNotFoundException ex) {
            return Response.status(Status.BAD_REQUEST).entity(ex.getMessage()).build();
//        } catch (InvalidLoginCredentialException ex) {
//            return Response.status(Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

}
