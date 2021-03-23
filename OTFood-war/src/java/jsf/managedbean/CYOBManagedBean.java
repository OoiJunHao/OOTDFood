/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import HelperClasses.IngredientCount;
import ejb.session.stateless.IngredientEntitySessionBeanLocal;
import entity.CYOBEntity;
import entity.IngredientEntity;
import entity.MealEntity;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.PrimeFaces;
import org.primefaces.event.DragDropEvent;
import util.enumeration.CategoryEnum;
import util.enumeration.IngredientTypeEnum;

/**
 *
 * @author benny
 */
@Named(value = "cYOBManagedBean")
@ViewScoped
public class CYOBManagedBean implements Serializable {

    @EJB(name = "IngredientEntitySessionBeanLocal")
    private IngredientEntitySessionBeanLocal ingredientEntitySessionBeanLocal;

    @Inject
    private cartManagedBean cartManagedBean;

    private List<IngredientEntity> bases;
    private List<IngredientEntity> meats;
    private List<IngredientEntity> vegetables;
    private List<IngredientEntity> sauces;
    private List<IngredientEntity> addons;
    private List<IngredientCount> droppedIngredientsForBases;
    private List<IngredientCount> droppedIngredientsForMeats;
    private List<IngredientCount> droppedIngredientsForVeges;
    private List<IngredientCount> droppedIngredientsForSauces;
    private List<IngredientCount> droppedIngredientsForAddons;
    private Map<IngredientEntity, Integer> droppedIngredientsWithCount;
    private int totalCalorie;
    private BigDecimal totalPrice;

    /**
     * Creates a new instance of CYOBManagedBean
     */
    public CYOBManagedBean() {
        totalCalorie = 0;
        totalPrice = new BigDecimal(0);
        droppedIngredientsForBases = new ArrayList<>();
        droppedIngredientsForMeats = new ArrayList<>();
        droppedIngredientsForVeges = new ArrayList<>();
        droppedIngredientsForSauces = new ArrayList<>();
        droppedIngredientsForAddons = new ArrayList<>();
        droppedIngredientsWithCount = new HashMap<IngredientEntity, Integer>();
    }

    public void onDropBase(DragDropEvent<IngredientEntity> ddEvent) {
        IngredientEntity product = ddEvent.getData();
        IngredientCount newIngredient = new IngredientCount(product, 1);
        droppedIngredientsForBases.add(newIngredient);
        bases.remove(product);
        MathContext mc = new MathContext(10);
        totalPrice = totalPrice.add(product.getPrice(), mc);
        totalCalorie += product.getCalorie();

    }

    public void onDropMeat(DragDropEvent<IngredientEntity> ddEvent) {
        IngredientEntity product = ddEvent.getData();
        IngredientCount newIngredient = new IngredientCount(product, 1);
        droppedIngredientsForMeats.add(newIngredient);
        meats.remove(product);
        MathContext mc = new MathContext(10);
        totalPrice = totalPrice.add(product.getPrice(), mc);
        totalCalorie += product.getCalorie();
    }

    public void onDropVege(DragDropEvent<IngredientEntity> ddEvent) {
        IngredientEntity product = ddEvent.getData();
        IngredientCount newIngredient = new IngredientCount(product, 1);
        droppedIngredientsForVeges.add(newIngredient);
        vegetables.remove(product);
        MathContext mc = new MathContext(10);
        totalPrice = totalPrice.add(product.getPrice(), mc);
        totalCalorie += product.getCalorie();
    }

    public void onDropSauce(DragDropEvent<IngredientEntity> ddEvent) {
        IngredientEntity product = ddEvent.getData();
        IngredientCount newIngredient = new IngredientCount(product, 1);
        droppedIngredientsForSauces.add(newIngredient);
        sauces.remove(product);
        MathContext mc = new MathContext(10);
        totalPrice = totalPrice.add(product.getPrice(), mc);
        totalCalorie += product.getCalorie();
    }

    public void onDropAddon(DragDropEvent<IngredientEntity> ddEvent) {
        IngredientEntity product = ddEvent.getData();
        IngredientCount newIngredient = new IngredientCount(product, 1);
        droppedIngredientsForAddons.add(newIngredient);
        addons.remove(product);
        MathContext mc = new MathContext(10);
        totalPrice = totalPrice.add(product.getPrice(), mc);
        totalCalorie += product.getCalorie();

    }

    public void onRemoveBases(ActionEvent event) {
        IngredientEntity ingredient = (IngredientEntity) event.getComponent().getAttributes().get("ingredient");
        String name = ingredient.getName();
        for (int i = 0; i < droppedIngredientsForBases.size(); i++) {
            if (droppedIngredientsForBases.get(i).getIngredient().getName().equals(name)) {
                int count = droppedIngredientsForBases.get(i).getCount();
                droppedIngredientsForBases.remove(i);
                bases.add(ingredient);
                for (int j = 0; j < count; j++) {
                    MathContext mc = new MathContext(10);
                    totalPrice = totalPrice.subtract(ingredient.getPrice(), mc);
                    totalCalorie -= ingredient.getCalorie();
                }
                break;
            }
        }
        PrimeFaces.current().ajax().update("allBases");
        PrimeFaces.current().ajax().update("selectedBases");
    }

    public void onRemoveMeats(ActionEvent event) {
        IngredientEntity ingredient = (IngredientEntity) event.getComponent().getAttributes().get("ingredient");
        String name = ingredient.getName();
        for (int i = 0; i < droppedIngredientsForMeats.size(); i++) {
            if (droppedIngredientsForMeats.get(i).getIngredient().getName().equals(name)) {
                int count = droppedIngredientsForMeats.get(i).getCount();
                droppedIngredientsForMeats.remove(i);
                meats.add(ingredient);
                for (int j = 0; j < count; j++) {
                    MathContext mc = new MathContext(10);
                    totalPrice = totalPrice.subtract(ingredient.getPrice(), mc);
                    totalCalorie -= ingredient.getCalorie();
                }
                break;
            }
        }
        PrimeFaces.current().ajax().update("allMeats");
        PrimeFaces.current().ajax().update("selectedMeats");
    }

    public void onRemoveVeges(ActionEvent event) {
        IngredientEntity ingredient = (IngredientEntity) event.getComponent().getAttributes().get("ingredient");
        String name = ingredient.getName();
        for (int i = 0; i < droppedIngredientsForVeges.size(); i++) {
            if (droppedIngredientsForVeges.get(i).getIngredient().getName().equals(name)) {
                int count = droppedIngredientsForVeges.get(i).getCount();
                droppedIngredientsForVeges.remove(i);
                vegetables.add(ingredient);
                for (int j = 0; j < count; j++) {
                    MathContext mc = new MathContext(10);
                    totalPrice = totalPrice.subtract(ingredient.getPrice(), mc);
                    totalCalorie -= ingredient.getCalorie();
                }
                break;
            }
        }

        PrimeFaces.current().ajax().update("allVeges");
        PrimeFaces.current().ajax().update("selectedVeges");

    }

    public void onRemoveSauces(ActionEvent event) {
        IngredientEntity ingredient = (IngredientEntity) event.getComponent().getAttributes().get("ingredient");
        String name = ingredient.getName();
        for (int i = 0; i < droppedIngredientsForSauces.size(); i++) {
            if (droppedIngredientsForSauces.get(i).getIngredient().getName().equals(name)) {
                int count = droppedIngredientsForSauces.get(i).getCount();
                droppedIngredientsForSauces.remove(i);
                sauces.add(ingredient);
                for (int j = 0; j < count; j++) {
                    MathContext mc = new MathContext(10);
                    totalPrice = totalPrice.subtract(ingredient.getPrice(), mc);
                    totalCalorie -= ingredient.getCalorie();
                }
                break;
            }
        }
        PrimeFaces.current().ajax().update("allSauces");
        PrimeFaces.current().ajax().update("selectedSauces");
    }

    public void onRemoveAddons(ActionEvent event) {
        IngredientEntity ingredient = (IngredientEntity) event.getComponent().getAttributes().get("ingredient");
        String name = ingredient.getName();
        for (int i = 0; i < droppedIngredientsForAddons.size(); i++) {
            if (droppedIngredientsForAddons.get(i).getIngredient().getName().equals(name)) {
                int count = droppedIngredientsForAddons.get(i).getCount();
                droppedIngredientsForAddons.remove(i);
                addons.add(ingredient);
                for (int j = 0; j < count; j++) {
                    MathContext mc = new MathContext(10);
                    totalPrice = totalPrice.subtract(ingredient.getPrice(), mc);
                    totalCalorie -= ingredient.getCalorie();
                }
                break;
            }
        }
        PrimeFaces.current().ajax().update("allAddons");
        PrimeFaces.current().ajax().update("selectedAddons");
    }

    public void addtoCart() {
        List<IngredientEntity> ingredients = new ArrayList<>();
        String description = "Bases:\n";
        for (int i = 0; i < droppedIngredientsForBases.size(); i++) {
            for (int j = 0; j < droppedIngredientsForBases.get(i).getCount(); j++) {
                ingredients.add(droppedIngredientsForBases.get(i).getIngredient());
            }
            description += " \t\u2022 " + droppedIngredientsForBases.get(i).getCount() + " " + droppedIngredientsForBases.get(i).getIngredient().getName() + "\n";
        }
        description += "Meats:\n";
        for (int i = 0; i < droppedIngredientsForMeats.size(); i++) {
            for (int j = 0; j < droppedIngredientsForMeats.get(i).getCount(); j++) {
                ingredients.add(droppedIngredientsForMeats.get(i).getIngredient());
            }
            description += "  \t\u2022 " + droppedIngredientsForMeats.get(i).getCount() + " " + droppedIngredientsForMeats.get(i).getIngredient().getName() + "\n";
        }
        description += "Veges:\n";
        for (int i = 0; i < droppedIngredientsForVeges.size(); i++) {
            for (int j = 0; j < droppedIngredientsForVeges.get(i).getCount(); j++) {
                ingredients.add(droppedIngredientsForVeges.get(i).getIngredient());
            }
            description += "  \t\u2022 " + droppedIngredientsForVeges.get(i).getCount() + " " + droppedIngredientsForVeges.get(i).getIngredient().getName() + "\n";
        }
        description += "Sauces:\n";
        for (int i = 0; i < droppedIngredientsForSauces.size(); i++) {
            for (int j = 0; j < droppedIngredientsForSauces.get(i).getCount(); j++) {
                ingredients.add(droppedIngredientsForSauces.get(i).getIngredient());
            }
            description += "  \t\u2022 " + droppedIngredientsForSauces.get(i).getCount() + " " + droppedIngredientsForSauces.get(i).getIngredient().getName() + "\n";
        }
        description += "Add ons:\n";
        for (int i = 0; i < droppedIngredientsForAddons.size(); i++) {
            for (int j = 0; j < droppedIngredientsForAddons.get(i).getCount(); j++) {
                ingredients.add(droppedIngredientsForAddons.get(i).getIngredient());
            }
            description += "  \t\u2022 " + droppedIngredientsForAddons.get(i).getCount() + " " + droppedIngredientsForAddons.get(i).getIngredient().getName() + "\n";
        }
        //MealEntity newMeal = new CYOBEntity("CYOB", totalPrice, "New CYOB", false, totalCalorie);
        //newMeal.setIngredients(ingredients);
        //push to cart
        if (ingredients.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter at least 1 ingredient!", null));
            return;
        }

        MealEntity newMeal = new CYOBEntity("CYOB", totalPrice, description, totalCalorie, "CYOB", new ArrayList<CategoryEnum>());
        newMeal.setIngredients(ingredients);
        this.cartManagedBean.addCYOBToCart(newMeal);
        PrimeFaces.current().ajax().update("cartForm");

    }

    public void addQuantityToList(ActionEvent event) {
        System.out.print("TEST");
        IngredientEntity ingredient = (IngredientEntity) event.getComponent().getAttributes().get("ingredient");
        String name = ingredient.getName();
        IngredientTypeEnum type = ingredient.getType();
        if (type == IngredientTypeEnum.BASE) {
            for (IngredientCount ingredientCount : droppedIngredientsForBases) {
                if (ingredientCount.getIngredient().getName().equals(name)) {
                    int currentCount = ingredientCount.getCount();
                    int newCount = ++currentCount;
                    ingredientCount.setCount(newCount);
                    break;
                }
            }
        }
        if (type == IngredientTypeEnum.MEAT) {
            for (IngredientCount ingredientCount : droppedIngredientsForMeats) {
                if (ingredientCount.getIngredient().getName().equals(name)) {
                    int currentCount = ingredientCount.getCount();
                    int newCount = ++currentCount;
                    ingredientCount.setCount(newCount);
                    break;
                }
            }
        }
        if (type == IngredientTypeEnum.VEGE) {
            for (IngredientCount ingredientCount : droppedIngredientsForVeges) {
                if (ingredientCount.getIngredient().getName().equals(name)) {
                    int currentCount = ingredientCount.getCount();
                    int newCount = ++currentCount;
                    ingredientCount.setCount(newCount);
                    break;
                }
            }
        }
        if (type == IngredientTypeEnum.SAUCE) {
            for (IngredientCount ingredientCount : droppedIngredientsForSauces) {
                if (ingredientCount.getIngredient().getName().equals(name)) {
                    int currentCount = ingredientCount.getCount();
                    int newCount = ++currentCount;
                    ingredientCount.setCount(newCount);
                    break;
                }
            }
        }
        if (type == IngredientTypeEnum.ADDON) {
            for (IngredientCount ingredientCount : droppedIngredientsForAddons) {
                if (ingredientCount.getIngredient().getName().equals(name)) {
                    int currentCount = ingredientCount.getCount();
                    int newCount = ++currentCount;
                    ingredientCount.setCount(newCount);
                    break;
                }
            }
        }
        MathContext mc = new MathContext(10);
        totalPrice = totalPrice.add(ingredient.getPrice(), mc);
        totalCalorie += ingredient.getCalorie();
    }

    @PostConstruct
    public void postConstruct() {
        bases = ingredientEntitySessionBeanLocal.retrieveListOfBases();
        meats = ingredientEntitySessionBeanLocal.retrieveListOfMeats();
        vegetables = ingredientEntitySessionBeanLocal.retrieveListOfVegetables();
        sauces = ingredientEntitySessionBeanLocal.retrieveListOfSauces();
        addons = ingredientEntitySessionBeanLocal.retrieveListOfAddons();
    }

    public List<Map.Entry<IngredientEntity, Integer>> getIngredients() {
        Set<Map.Entry<IngredientEntity, Integer>> productSet
                = this.droppedIngredientsWithCount.entrySet();
        return new ArrayList<Map.Entry<IngredientEntity, Integer>>(productSet);
    }

    public List<IngredientEntity> getBases() {
        return bases;
    }

    public void setBases(List<IngredientEntity> bases) {
        this.bases = bases;
    }

    public List<IngredientEntity> getMeats() {
        return meats;
    }

    public void setMeats(List<IngredientEntity> meats) {
        this.meats = meats;
    }

    public List<IngredientEntity> getVegetables() {
        return vegetables;
    }

    public void setVegetables(List<IngredientEntity> vegetables) {
        this.vegetables = vegetables;
    }

    public List<IngredientEntity> getSauces() {
        return sauces;
    }

    public void setSauces(List<IngredientEntity> sauces) {
        this.sauces = sauces;
    }

    public List<IngredientEntity> getAddons() {
        return addons;
    }

    public void setAddons(List<IngredientEntity> addons) {
        this.addons = addons;
    }

    public List<IngredientCount> getDroppedIngredientsForBases() {
        return droppedIngredientsForBases;
    }

    public void setDroppedIngredientsForBases(List<IngredientCount> droppedIngredientsForBases) {
        this.droppedIngredientsForBases = droppedIngredientsForBases;
    }

    public List<IngredientCount> getDroppedIngredientsForMeats() {
        return droppedIngredientsForMeats;
    }

    public void setDroppedIngredientsForMeats(List<IngredientCount> droppedIngredientsForMeats) {
        this.droppedIngredientsForMeats = droppedIngredientsForMeats;
    }

    public List<IngredientCount> getDroppedIngredientsForVeges() {
        return droppedIngredientsForVeges;
    }

    public void setDroppedIngredientsForVeges(List<IngredientCount> droppedIngredientsForVeges) {
        this.droppedIngredientsForVeges = droppedIngredientsForVeges;
    }

    public List<IngredientCount> getDroppedIngredientsForSauces() {
        return droppedIngredientsForSauces;
    }

    public void setDroppedIngredientsForSauces(List<IngredientCount> droppedIngredientsForSauces) {
        this.droppedIngredientsForSauces = droppedIngredientsForSauces;
    }

    public List<IngredientCount> getDroppedIngredientsForAddons() {
        return droppedIngredientsForAddons;
    }

    public void setDroppedIngredientsForAddons(List<IngredientCount> droppedIngredientsForAddons) {
        this.droppedIngredientsForAddons = droppedIngredientsForAddons;
    }

    public Map<IngredientEntity, Integer> getDroppedIngredientsWithCount() {
        return droppedIngredientsWithCount;
    }

    public void setDroppedIngredientsWithCount(Map<IngredientEntity, Integer> droppedIngredientsWithCount) {
        this.droppedIngredientsWithCount = droppedIngredientsWithCount;
    }

    public int getTotalCalorie() {
        return totalCalorie;
    }

    public void setTotalCalorie(int totalCalorie) {
        this.totalCalorie = totalCalorie;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

}
