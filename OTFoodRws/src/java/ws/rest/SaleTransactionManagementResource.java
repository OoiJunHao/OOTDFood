/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.SaleTransactionEntitySessionBeanLocal;
import ejb.session.stateless.StaffEntitySessionBeanLocal;
import entity.SaleTransactionEntity;
import entity.SaleTransactionLineEntity;
import entity.StaffEntity;
    import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import util.exception.InvalidLoginCredentialException;
import ws.datamodel.ReportReq;

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
    
    @Path("generateReport")
    @POST
    @Produces(MediaType.MULTIPART_FORM_DATA)
    public Response generateReport(ReportReq reportReq) {
           try {

            System.out.println("PRINTING REPORT------------------------->   ");
            HashMap parameters = new HashMap();
            /*
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date date = formatter.parse("01-12-2020 23:59:00");
            Date date2 = formatter.parse("31-12-2020 23:59:00");
            Timestamp ts =new Timestamp(date.getTime());  */
            parameters.put("STARTDATE", reportReq.getStart());
            parameters.put("ENDDATE", reportReq.getEnd());

            JasperRunManager.runReportToPdfFile("C:/glassfish-5.1.0-uploadedfiles/uploadedFiles/report/SaleTransactionReport.jasper","C:/glassfish-5.1.0-uploadedfiles/uploadedFiles/report/report.pdf", parameters, getOTFoodDataSource().getConnection());
            return Response.status(Response.Status.OK).build();
        } catch (JRException ex) {
            ex.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        } catch (NamingException ex) {
            Logger.getLogger(SaleTransactionManagementResource.class.getName()).log(Level.SEVERE, null, ex);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    private DataSource getOTFoodDataSource() throws NamingException {
        javax.naming.Context c = new InitialContext();
        return (DataSource) c.lookup("java:comp/env/OTFoodDataSource");
    }

    
}
