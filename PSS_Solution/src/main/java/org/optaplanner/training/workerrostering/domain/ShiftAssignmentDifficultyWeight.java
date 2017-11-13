/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.optaplanner.training.workerrostering.domain;

import java.util.Comparator;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionSorterWeightFactory;

/**
 *
 * @author r128
 */
public class ShiftAssignmentDifficultyWeight implements Comparator<ShiftAssignment> {

    public int compare(ShiftAssignment a, ShiftAssignment b) {
        return new CompareToBuilder().append(a.getEmployee() , b.getEmployee()).toComparison();          
    }
    
}
