/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.MealEntitySessionBeanLocal;
import entity.BentoEntity;
import java.io.Console;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.TabChangeEvent;

/**
 *
 * @author yuntiangu
 */
@Named(value = "viewBentoManagedBean")
@ViewScoped
public class ViewBentoManagedBean implements Serializable {

    @EJB
    private MealEntitySessionBeanLocal mealEntitySessionBeanLocal;

    private List<BentoEntity> listOfBentos;
    private List<String> listOfCategories;
    private String selectedCategory;

    @PostConstruct
    public void postConstruct() {
        this.listOfCategories = new ArrayList<>();
        this.listOfCategories.add("Chicken");
        this.listOfCategories.add("Fish");
        this.listOfCategories.add("Beef");
        this.listOfCategories.add("Pork");
        this.listOfCategories.add("Vegetarian");
        this.listOfCategories.add("Impossible");
        System.out.println(this.listOfCategories);
        this.setListOfBentos(mealEntitySessionBeanLocal.retriveAllBentos());
    }

    public ViewBentoManagedBean() {
        this.listOfBentos = new ArrayList<>();
        
        this.selectedCategory = "";
    }
    
    public void refreshListByTabSelected(TabChangeEvent event) {
        System.out.println("List Refreshsed");
        String currentTabId = event.getTab().getTitle(); //By right tab Id should be the category
        System.out.println("tab Title: " + currentTabId);
        this.listOfBentos = mealEntitySessionBeanLocal.retrieveBentosByCategory(currentTabId);

    }

    public void getListOfBentosByCategory(AjaxBehaviorEvent event) {
        this.setListOfBentos(mealEntitySessionBeanLocal.retrieveBentosByCategory(getSelectedCategory()));
    }

    /**
     * @return the listOfBentos
     */
    public List<BentoEntity> getListOfBentos() {
        return listOfBentos;
    }

    /**
     * @param listOfBentos the listOfBentos to set
     */
    public void setListOfBentos(List<BentoEntity> listOfBentos) {
        this.listOfBentos = listOfBentos;
    }

    /**
     * @return the listOfCategories
     */
    public List<String> getListOfCategories() {
        return listOfCategories;
    }

    /**
     * @param listOfCategories the listOfCategories to set
     */
    public void setListOfCategories(List<String> listOfCategories) {
        this.listOfCategories = listOfCategories;
    }

    /**
     * @return the selectedCategory
     */
    public String getSelectedCategory() {
        return selectedCategory;
    }

    /**
     * @param selectedCategory the selectedCategory to set
     */
    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }
}
