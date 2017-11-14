/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.optaplanner.training.workerrostering.domain;

import java.time.LocalDateTime;
import java.util.Comparator;

/**
 *
 * @author r128
 */
public class ShiftAssignmentDateComparator implements Comparator<ShiftAssignment> {
    public int compare(ShiftAssignment shiftA, ShiftAssignment shiftB) {
        int startA = 4;
        int startB = 4;
        if (shiftA.getSpot().getName().toLowerCase().indexOf("woodlands") >= 0) {
            startA = "woodlands".length();
        }
        
        if (shiftB.getSpot().getName().toLowerCase().indexOf("woodlands") >= 0) {
            startB = "woodlands".length();
        }
        
        String strShiftANum = shiftA.getSpot().getName().substring(startA, shiftA.getSpot().getName().length());
        String strShiftBNum = shiftB.getSpot().getName().substring(startB, shiftB.getSpot().getName().length());
        int shiftANum = Integer.parseInt(strShiftANum);
        int shiftBNum = Integer.parseInt(strShiftBNum);
        
        LocalDateTime shiftAStartTime = shiftA.getTimeSlot().getStartDateTime();
        LocalDateTime shiftBStartTime = shiftB.getTimeSlot().getStartDateTime();
        
        if (shiftAStartTime.compareTo(shiftBStartTime) > 0) {
            return 1;
        } else if (shiftAStartTime.compareTo(shiftBStartTime) < 0) {
            return -1;
        } else {
            return shiftANum - shiftBNum;
        }
    }
}

