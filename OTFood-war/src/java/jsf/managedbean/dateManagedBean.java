/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Mitsuki
 */
@Named(value = "dateManagedBean")
@ViewScoped
public class dateManagedBean implements Serializable {

    private Date minDate;

    public dateManagedBean() {
        minDate = new GregorianCalendar(1950, Calendar.JANUARY, 1).getTime();
    }

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    /**
     * Creates a new instance of dateManagedBean
     */
}
