/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.SaleTransactionEntitySessionBeanLocal;
import ejb.session.stateless.StaffEntitySessionBeanLocal;
import entity.ReviewEntity;
import entity.SaleTransactionEntity;
import entity.SaleTransactionLineEntity;
import entity.StaffEntity;
import java.util.List;
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
 * @author Ooi Jun Hao
 */
@Path("saleTransactionManagement")
public class SaleTransactionManagementResource {

    private final SaleTransactionEntitySessionBeanLocal saleTransactionEntitySessionBean;
    private final SessionBeanLookup sessionBeanLookUp;
    private final StaffEntitySessionBeanLocal staffEntitySessionBeanLocal;
    
    @Context
    private UriInfo context;

    public SaleTransactionManagementResource() {
        sessionBeanLookUp = new SessionBeanLookup();
        saleTransactionEntitySessionBean = sessionBeanLookUp.saleTransactionEntitySessionBean;
        staffEntitySessionBeanLocal = sessionBeanLookUp.staffEntitySessionBean;
    }

    @Path("retrieveAllSaleTransactions")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllReviews(@QueryParam("username") String username,
            @QueryParam("password") String password) {
        
        try {
            StaffEntity staff = staffEntitySessionBeanLocal.staffLogin(username, password);
            System.out.println("********** ReviewManagent.retrieveAllReviews(): Staff " + staff.getUsername() + " login remotely via web service");

            List<SaleTransactionEntity> saleTransactions = saleTransactionEntitySessionBean.retrieveAllSaleTransaction();

            for (SaleTransactionEntity st : saleTransactions) {
                st.getUser().getSaleTransaction().clear();
                st.getUser().getReviews().clear();
                st.getUser().getCreditCard().clear();
                st.getUser().getAddress().clear();
                
                for (SaleTransactionLineEntity stle: st.getSaleTransactionLineItemEntities()) {
                    stle.getMeal().getReviews().clear();
                }
            }
            GenericEntity<List<SaleTransactionEntity>> genericSales = new GenericEntity<List<SaleTransactionEntity>>(saleTransactions) {};
            return Response.status(Response.Status.OK).entity(genericSales).build();
        } catch (InvalidLoginCredentialException ex) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(ex.getMessage()).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }
    
}
