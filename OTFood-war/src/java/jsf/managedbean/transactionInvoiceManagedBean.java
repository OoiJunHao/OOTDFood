/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

/**
 *
 * @author Ooi Jun Hao
 */
@Named(value = "transactionInvoiceManagedBean")
@RequestScoped
public class transactionInvoiceManagedBean {

    @Resource(name = "OTFoodDataSource")
    private DataSource oTFoodDataSource;

    private long selectedTransactionID;

    /**
     * Creates a new instance of transactionInvoiceManagedBean
     */
    public transactionInvoiceManagedBean() {
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("Creating this transactionInvoiceManagedBean");

    }

    public void generateReport() {
        try {

            System.out.println("PRINTING RECEIPT------------------------->   " + this.selectedTransactionID);
            HashMap parameters = new HashMap();

            parameters.put("SaleTransactionID", this.selectedTransactionID);
            //FacesContext.getCurrentInstance().responseComplete();

            InputStream reportStream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasperreport/transactionInvoice.jasper");
            OutputStream outputStream = FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream();

            //JasperRunManager.runReportToPdfFile(" /jasperreport/transactionInvoice.jasper", "test", parameters, otFoodDataSource.getConnection());
            JasperRunManager.runReportToPdfStream(reportStream, outputStream, parameters, oTFoodDataSource.getConnection());
            //FacesContext.getCurrentInstance().responseComplete();

        } catch (JRException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
        }
    }

    /**
     * @return the selectedTransactionID
     */
    public long getSelectedTransactionID() {
        return selectedTransactionID;
    }

    /**
     * @param selectedTransactionID the selectedTransactionID to set
     */
    public void setSelectedTransactionID(long selectedTransactionID) {
        this.selectedTransactionID = selectedTransactionID;
    }
}
