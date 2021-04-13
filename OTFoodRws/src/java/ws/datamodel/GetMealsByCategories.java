/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import java.util.List;
import util.enumeration.CategoryEnum;

/**
 *
 * @author benny
 */
public class GetMealsByCategories {
    private String username;
    private String password;
    private List<CategoryEnum> enums;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<CategoryEnum> getEnums() {
        return enums;
    }

    public void setEnums(List<CategoryEnum> enums) {
        this.enums = enums;
    }
    
    
}
