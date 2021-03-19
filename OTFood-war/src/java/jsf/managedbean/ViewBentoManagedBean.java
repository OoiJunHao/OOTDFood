/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.MealEntitySessionBeanLocal;
import entity.BentoEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;
import org.primefaces.event.TabChangeEvent;
import util.enumeration.CategoryEnum;

/**
 *
 * @author yuntiangu
 */
@Named(value = "viewBentoManagedBean")
@RequestScoped
public class ViewBentoManagedBean implements Serializable {

    @EJB
    private MealEntitySessionBeanLocal mealEntitySessionBeanLocal;

    private List<BentoEntity> listOfBentos;
    private List<BentoEntity> listOfAllOfOurFuckingBentos;
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
        this.setListOfAllOfOurFuckingBentos(mealEntitySessionBeanLocal.retriveAllBentos());
    }

    public ViewBentoManagedBean() {
        this.selectedCategory = "";
        this.listOfBentos = new ArrayList<>();   
    }
    
    public void refreshListByTabSelected(TabChangeEvent event) {
        System.out.println("List Refreshed");
        String currentTabId = event.getTab().getTitle(); //This will be the CategoryEnum
        System.out.println("tab Title: " + currentTabId);
        setSelectedCategory(event.getTab().getTitle());
        System.out.println("current category " + getSelectedCategory());
        setListOfBentos(mealEntitySessionBeanLocal.retrieveBentosByCategory(currentTabId));
        System.out.println("No of Bentos: " + this.listOfBentos.toString());
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

    /**
     * @return the listOfAllOfOurFuckingBentos
     */
    public List<BentoEntity> getListOfAllOfOurFuckingBentos() {
        return listOfAllOfOurFuckingBentos;
    }

    /**
     * @param listOfAllOfOurFuckingBentos the listOfAllOfOurFuckingBentos to set
     */
    public void setListOfAllOfOurFuckingBentos(List<BentoEntity> listOfAllOfOurFuckingBentos) {
        this.listOfAllOfOurFuckingBentos = listOfAllOfOurFuckingBentos;
    }
}
