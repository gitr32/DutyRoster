/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.optaplanner.training.workerrostering.domain;

import java.io.Serializable;
import java.util.Comparator;
import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 *
 * @author r128
 */
public class ShiftAssignmentStrengthComparator implements Comparator<ShiftAssignment>, Serializable {
    public int compare(ShiftAssignment a, ShiftAssignment b) {
        return new CompareToBuilder()
                .append(a.getEmployee(), b.getEmployee())
                .toComparison();
    }
}

