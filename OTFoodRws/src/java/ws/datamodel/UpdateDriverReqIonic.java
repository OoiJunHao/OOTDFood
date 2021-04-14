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
public class UpdateDriverReqIonic {

    DriverEntity toUpdateDriver;

    public UpdateDriverReqIonic() {
    }

    public UpdateDriverReqIonic(DriverEntity toUpdateDriver) {
        this.toUpdateDriver = toUpdateDriver;
    }

    public DriverEntity getToUpdateDriver() {
        return toUpdateDriver;
    }

    public void setToUpdateDriver(DriverEntity toUpdateDriver) {
        this.toUpdateDriver = toUpdateDriver;
    }

}
