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
import util.enumeration.CategoryEnum;

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
    private List<CategoryEnum> listOfCategories;
    private String selectedCategory;

    @PostConstruct
    public void postConstruct() {
        this.listOfCategories = new ArrayList<>();
        this.listOfCategories.add(CategoryEnum.CHICKEN);
        this.listOfCategories.add(CategoryEnum.FISH);
        this.listOfCategories.add(CategoryEnum.BEEF);
        this.listOfCategories.add(CategoryEnum.PORK);
        this.listOfCategories.add(CategoryEnum.VEGETARIAN);
        this.listOfCategories.add(CategoryEnum.VEGAN);
        this.listOfCategories.add(CategoryEnum.BALANCED);
        System.out.println(this.listOfCategories);
        this.setListOfBentos(mealEntitySessionBeanLocal.retriveAllBentos());
    }

    public ViewBentoManagedBean() {
        this.listOfBentos = new ArrayList<>();
        this.selectedCategory = "";
    }
    
    public void refreshListByTabSelected(TabChangeEvent event) {
        System.out.println("List Refreshed");
        String currentTabId = event.getTab().getTitle(); //This will be the CategoryEnum
        System.out.println("tab Title: " + currentTabId);
        this.listOfBentos = mealEntitySessionBeanLocal.retrieveBentosByCategory(currentTabId);
        System.out.println("No of Bentos: " + this.listOfBentos.size());
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
    public List<CategoryEnum> getListOfCategories() {
        return listOfCategories;
    }

    /**
     * @param listOfCategories the listOfCategories to set
     */
    public void setListOfCategories(List<CategoryEnum> listOfCategories) {
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
