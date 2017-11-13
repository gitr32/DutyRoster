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
public class ShiftAssignmentSpotComparator implements Comparator<ShiftAssignment> {
    public int compare(ShiftAssignment shiftA, ShiftAssignment shiftB) {
        String strShiftANum = shiftA.getSpot().getName().substring(0, shiftA.getSpot().getName().length());
        String strShiftBNum = shiftB.getSpot().getName().substring(0, shiftB.getSpot().getName().length());
        int shiftANum = Integer.parseInt(strShiftANum);
        int shiftBNum = Integer.parseInt(strShiftBNum);
        
        LocalDateTime shiftAStartTime = shiftA.getTimeSlot().getStartDateTime();
        LocalDateTime shiftBStartTime = shiftB.getTimeSlot().getStartDateTime();
        
        if (shiftANum > shiftBNum) {
            return 1;
        } else if (shiftANum < shiftBNum) {
            return -1;
        } else {
            return shiftAStartTime.compareTo(shiftBStartTime);
        }
    }
}
