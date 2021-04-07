/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.datamodel;

import java.util.Date;

/**
 *
 * @author Ooi Jun Hao
 */
public class ReportReq {
    private Date start;
    private Date end;

    public ReportReq(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    public ReportReq() {
    }

    /**
     * @return the start
     */
    public Date getStart() {
        return start;
    }

    /**
     * @param start the start to set
     */
    public void setStart(Date start) {
        this.start = start;
    }

    /**
     * @return the end
     */
    public Date getEnd() {
        return end;
    }

    /**
     * @param end the end to set
     */
    public void setEnd(Date end) {
        this.end = end;
    }
    
    
    
}
