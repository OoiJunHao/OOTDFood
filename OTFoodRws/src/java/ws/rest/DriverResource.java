/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.DriverEntitySessionBeanLocal;
import ejb.session.stateless.SaleTransactionEntitySessionBeanLocal;
import entity.DriverEntity;
import entity.SaleTransactionEntity;
import entity.SaleTransactionLineEntity;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import util.exception.DriverAlreadyFoundException;
import util.exception.DriverExistsException;
import util.exception.DriverNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.NoSaleTransactionFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateDriverException;
import ws.datamodel.CreateDriverReq;
import ws.datamodel.ImageWrapper;
import ws.datamodel.UpdateDriverReqIonic;

/**
 * REST Web Service
 *
 * @author Ong Bik Jeun
 */
@Path("Driver")
public class DriverResource {

    DriverEntitySessionBeanLocal driverEntitySessionBean;
    SaleTransactionEntitySessionBeanLocal saleTransactionEntitySessionBean;
    private final SessionBeanLookup sessionBeanLookup;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of DriverResource
     */
    public DriverResource() {
        sessionBeanLookup = new SessionBeanLookup();
        driverEntitySessionBean = sessionBeanLookup.driverEntitySessionBean;
        saleTransactionEntitySessionBean = sessionBeanLookup.saleTransactionEntitySessionBean;
    }

    @Path("driverLogin")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response driverLogin(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        try {
            DriverEntity driver = driverEntitySessionBean.driverLogin(username, password);
            driver.getSaleTransaction().clear();
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
                DriverEntity driver = driverEntitySessionBean.createNewDriver(createDriverReq.getNewDriver());
                driver.getSaleTransaction().clear();
                return Response.status(Response.Status.OK).entity(driver).build();
            } catch (UnknownPersistenceException | InputDataValidationException | DriverExistsException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid create new driver request").build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDriver(UpdateDriverReqIonic updateDriverReq) {
        if (updateDriverReq != null) {
            try {
                DriverEntity updatedDriver = driverEntitySessionBean.updateDriverIonic(updateDriverReq.getToUpdateDriver());
                updatedDriver.getSaleTransaction().clear();
                return Response.status(Response.Status.OK).entity(updatedDriver).build();
            } catch (UpdateDriverException | DriverNotFoundException | InputDataValidationException ex) {
                return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid request").build();
        }
    }

    @Path("getDriverTransactionDeliveries")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDriverTransactionDeliveries(@QueryParam("driverId") String driverId) {
        try {
            DriverEntity queryDriver = driverEntitySessionBean.retrieveDriverById(Long.valueOf(driverId));
            List<SaleTransactionEntity> driverTransactions = queryDriver.getSaleTransaction();
            for (SaleTransactionEntity st : driverTransactions) {
                st.getSaleTransactionLineItemEntities().clear();
                st.getAddress().setUser(null);
                st.setCreditCardEntity(null);
                st.setPromoCode(null);
                st.setDriver(null);
                st.setUser(null);
            }
            driverTransactions.sort((y, x) -> x.getDeliveryDateTime().compareTo(y.getDeliveryDateTime()));
            GenericEntity<List<SaleTransactionEntity>> genericDriverTransactions = new GenericEntity<List<SaleTransactionEntity>>(driverTransactions) {
            };
            return Response.status(Response.Status.OK).entity(genericDriverTransactions).build();
        } catch (DriverNotFoundException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("setSaleToDriver/{driverId}/{customerId}/{saleTransactionId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDriverAndSale(@PathParam("driverId") long driverId, @PathParam("customerId") long customerId, @PathParam("saleTransactionId") long saleTransactionId) {
        try {
            DriverEntity driver = driverEntitySessionBean.setDriverToSaleTransaction(driverId, customerId, saleTransactionId);
            driver.getSaleTransaction().clear();
            return Response.status(Response.Status.OK).entity(driver).build();
        } catch (DriverNotFoundException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (NoSaleTransactionFoundException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (DriverAlreadyFoundException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @Path("completeDelivery/{driverId}/{saleTransactionId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response completeDelivery(@PathParam("driverId") long driverId, @PathParam("saleTransactionId") long saleTransactionId) {
        try {
            DriverEntity driver = driverEntitySessionBean.completeDelivery(driverId, saleTransactionId);
            driver.getSaleTransaction().clear();
            return Response.status(Response.Status.OK).entity(driver).build();
        } catch (DriverNotFoundException | NoSaleTransactionFoundException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    @Path("getCurrentDelivery/{driverId}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurrentDelivery(@PathParam("driverId") long driverId) {
        try {
            DriverEntity driver = this.driverEntitySessionBean.retrieveDriverById(driverId);
            if (driver.getCurrentDelivery() == 0l) {
                return Response.status(Response.Status.BAD_REQUEST).entity("No current delivery").build();
            } else {
                SaleTransactionEntity st = saleTransactionEntitySessionBean.retrieveSaleTransactionById(driver.getCurrentDelivery());
                st.getUser().getSaleTransaction().clear();
                st.getUser().getReviews().clear();
                st.getUser().getAddress().clear();
                st.getUser().getCreditCard().clear();
                st.setPromoCode(null);
                st.getDriver().getSaleTransaction().clear();
                for (SaleTransactionLineEntity stle : st.getSaleTransactionLineItemEntities()) {
                    stle.getMeal().getReviews().clear();
                }
                return Response.status(Response.Status.OK).entity(st).build();
            }
        } catch (DriverNotFoundException | NoSaleTransactionFoundException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }

    }

    @Path("uploadProfilePicture")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response uploadProfilePicture(@QueryParam("driverId") long driverId, String base64Image) {
        try {
            System.out.println("****** Uploading Profile Picture Image for Driver ID " + driverId + " ***************");
            DriverEntity driver = this.driverEntitySessionBean.retrieveDriverById(driverId);
            this.decoder(base64Image, String.valueOf(driver.getDriverId()));
            System.out.println("****** Profile Picture Image Upload for Driver ID " + driverId + " Complete ***************");
            return Response.status(Response.Status.OK).build();
        } catch (IOException ex) {
            System.out.println("Exception while reading the Image " + ex);
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (DriverNotFoundException ex) {
            Logger.getLogger(DriverResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }
    }

    public void decoder(String base64Image, String name) throws IOException {
        /*
         * MAC Users please change the file path here to the correct one
         */ 
        try (FileOutputStream imageOutFile = new FileOutputStream("C:/glassfish-5.1.0-uploadedFiles/uploadedFiles/drivers/" + name + ".jpg")) {
            // Converting a Base64 String into Image byte array
            base64Image = base64Image.replace("\n", "");
            String partSeparator = ",";
            if (base64Image.contains(partSeparator)) {
                String encodedImg = base64Image.split(partSeparator)[1];
                byte[] imageByteArray = Base64.getDecoder().decode(encodedImg);
                imageOutFile.write(imageByteArray);
            }
        }
    }

    @Path("retrieveProfilePicture")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveProfilePicture(@QueryParam("driverId") long driverId) {
        /*
         * MAC Users please change the file path here to the correct one
         */
        try {
            byte[] array = Files.readAllBytes(Paths.get("C:/glassfish-5.1.0-uploadedFiles/uploadedFiles/drivers/" + driverId + ".jpg"));
            String encodedString = Base64
                    .getEncoder()
                    .encodeToString(array);
            return Response.status(Response.Status.OK).entity(new ImageWrapper(encodedString)).build();
        } catch (IOException ex) {
            try {
                // it means image does not exist
                byte[] array = Files.readAllBytes(Paths.get("C:/glassfish-5.1.0-uploadedFiles/uploadedFiles/drivers/userDefault.png"));
                String encodedString = Base64
                        .getEncoder()
                        .encodeToString(array);
                return Response.status(Response.Status.OK).entity(new ImageWrapper(encodedString)).build();
            } catch (IOException ex1) {
                // it means really got error
                System.out.println("*************** Error in retrieving profile picture *************");
                ex.printStackTrace();
                return Response.status(Response.Status.BAD_REQUEST).entity(ex1.getMessage()).build();
            }
        }
    }

    @Path("cashOutEarnings")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response cashOutEarnings(@QueryParam("driverId") long driverId) {
        try {
            System.out.println("****** Cashing out for Driver ID " + driverId + " ***************");
            DriverEntity driver = driverEntitySessionBean.cashOutEarnings(driverId);
            driver.getSaleTransaction().clear();
            return Response.status(Response.Status.OK).entity(driver).build();
        } catch (DriverNotFoundException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        }

    }
}
