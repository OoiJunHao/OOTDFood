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
public class CreateDriverReq {

    private DriverEntity newDriver;

    public CreateDriverReq() {
    }

    public CreateDriverReq(DriverEntity driver) {
        this.newDriver = driver;
    }

    public DriverEntity getNewDriver() {
        return newDriver;
    }

    public void setNewDriver(DriverEntity newDriver) {
        this.newDriver = newDriver;
    }

}
