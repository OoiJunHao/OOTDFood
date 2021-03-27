/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.rest;

import ejb.session.stateless.MealEntitySessionBeanLocal;
import ejb.session.stateless.StaffEntitySessionBeanLocal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author benny
 */
public class SessionBeanLookup {

    MealEntitySessionBeanLocal mealEntitySessionBean = lookupMealEntitySessionBeanLocal();
    private final String ejbModuleJndiPath;

    public SessionBeanLookup() {
        ejbModuleJndiPath = "java:global/OTFood/OTFoodRws/";
    }

    public StaffEntitySessionBeanLocal lookupStaffEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (StaffEntitySessionBeanLocal) c.lookup("java:global/OTFood/OTFood-ejb/StaffEntitySessionBean!ejb.session.stateless.StaffEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    public MealEntitySessionBeanLocal lookupMealEntitySessionBeanLocal() {
        try {
            Context c = new InitialContext();
            return (MealEntitySessionBeanLocal) c.lookup("java:global/OTFood/OTFood-ejb/MealEntitySessionBean!ejb.session.stateless.MealEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
