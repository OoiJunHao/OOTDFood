/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.DriverEntity;

/**
 *
 * @author Ong Bik Jeun
 */
public class UpdateDriverReq {

    private String username;
    private String password;
    private DriverEntity driver;

    public UpdateDriverReq() {
    }

    public UpdateDriverReq(String username, String password, DriverEntity driver) {
        this.username = username;
        this.password = password;
        this.driver = driver;
    }

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

    public DriverEntity getDriver() {
        return driver;
    }

    public void setDriver(DriverEntity driver) {
        this.driver = driver;
    }

}
