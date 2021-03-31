/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HelperClasses;

import entity.MealEntity;
import java.util.Comparator;


/**
 *
 * @author benny
 */
public class SortByAvailability implements Comparator<MealEntity> {

    @Override
    public int compare(MealEntity o1, MealEntity o2) {
        if (o1.isIsAvailable() && !o2.isIsAvailable()) {
            return -1;
        } else if (!o1.isIsAvailable() && o2.isIsAvailable()) {
            return 1;
        } else {
            if(o1.getMealId() > o2.getMealId()) {
                return 1;
            } else if (o1.getMealId() < o2.getMealId()) {
                return -1;
            } else {
                return 0;
            }
        }
    }
    
}
