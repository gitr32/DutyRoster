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
public class EmployeeStrengthComparator implements Comparator<Employee>, Serializable {
    public int compare(Employee a, Employee b) {
        return new CompareToBuilder()
                .toComparison();
    }
}

