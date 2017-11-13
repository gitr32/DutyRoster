/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.sql.Timestamp;

/**
 *
 * @author User
 */
public class Time {
    
    private String nric;
    private Timestamp startTime;
    private Timestamp endTime;
    
    public Time(String nric, Timestamp startTime, Timestamp endTime){
        this.nric = nric;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getNric() {
        return nric;
    }

    public void setNric(String nric) {
        this.nric = nric;
    }
    
    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }
    
    
    
}
