/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import entity.StaffEntity;

/**
 *
 * @author Mitsuki
 */
public class UpdateStaffReq {

    private String username;
    private String password;
    private StaffEntity staff;

    public UpdateStaffReq() {
    }

    public UpdateStaffReq(String username, String password, StaffEntity staff) {
        this.username = username;
        this.password = password;
        this.staff = staff;
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

    public StaffEntity getStaff() {
        return staff;
    }

    public void setStaff(StaffEntity staff) {
        this.staff = staff;
    }

}
