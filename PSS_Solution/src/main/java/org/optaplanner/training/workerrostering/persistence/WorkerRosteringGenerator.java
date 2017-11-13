/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.optaplanner.training.workerrostering.persistence;

import bean.Time;
import controller.EmployeeController;
import dao.EmployeeDAO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.optaplanner.training.workerrostering.domain.Employee;
import org.optaplanner.training.workerrostering.domain.Roster;
import org.optaplanner.training.workerrostering.domain.RosterParametrization;
import org.optaplanner.training.workerrostering.domain.ShiftAssignment;
import org.optaplanner.training.workerrostering.domain.Skill;
import org.optaplanner.training.workerrostering.domain.Spot;
import org.optaplanner.training.workerrostering.domain.TimeSlot;
import org.optaplanner.training.workerrostering.domain.TimeSlotState;

public class WorkerRosteringGenerator {

    public static int numDays = 0;
    public static int numSpots = 0;

    public static void generateRoster(int numberOfDays, int numberOfSpots) {
//        new WorkerRosteringGenerator().generateAndWriteRoster(10, 7, false);
//        new WorkerRosteringGenerator().generateAndWriteRoster(10, 28, false);
//        new WorkerRosteringGenerator().generateAndWriteRoster(20, 28, false);
//        new WorkerRosteringGenerator().generateAndWriteRoster(40, 28 * 2, false);
//        new WorkerRosteringGenerator().generateAndWriteRoster(80, 28 * 4, false);
//        new WorkerRosteringGenerator().generateAndWriteRoster(10, 28, true);
//        new WorkerRosteringGenerator().generateAndWriteRoster(20, 28, true);
//        new WorkerRosteringGenerator().generateAndWriteRoster(40, 28 * 2, true);
//        new WorkerRosteringGenerator().generateAndWriteRoster(80, 28 * 4, true);
//        new WorkerRosteringGenerator().generateAndWriteRoster(34, 14, false);
        numDays = numberOfDays;
        numSpots = numberOfSpots;
        new WorkerRosteringGenerator().generateAndWriteRoster(numSpots, numberOfDays, false);
    }

    private final StringDataGenerator employeeNameGenerator = StringDataGenerator.build10kFullNames();
    private final StringDataGenerator spotNameGenerator = StringDataGenerator.build10kLocationNames();

    private String path = "C:\\Users\\User\\Desktop\\DutyRoster1\\DutyRoster\\optaplanner-training-lab902-solution\\data\\workerrostering\\";

    private final StringDataGenerator skillNameGenerator = new StringDataGenerator()
            .addPart(
                    "Mandarin",
                    "Malay",
                    "Tamil",
                    "Any",
                    "fSearch",
                    "mSearch");

    protected Random random = new Random(37);
    protected WorkerRosteringSolutionFileIO solutionFileIO = new WorkerRosteringSolutionFileIO();

    public void generateAndWriteRoster(int spotListSize, int dayListSize, boolean continuousPlanning) {
        Roster roster = generateRoster(spotListSize, dayListSize * 2, continuousPlanning);
        solutionFileIO.write(roster, new File(path + "import\\roster-"
                + spotListSize + "spots-" + dayListSize + "days"
                + (continuousPlanning ? "-continuous" : "") + ".xlsx"));
    }

    private int getNumberOfDaysForAMonth(int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, month);
//        return c.getActualMaximum(Calendar.DATE);
        return numDays;
    }
    
    public Roster generateRoster(int spotListSize, int timeSlotListSize, boolean continuousPlanning) {
        int employeeListSize = 136;
        if(spotListSize == 28){
            employeeListSize = 112;
        }
        
        int skillListSize = 6;
        timeSlotListSize = getNumberOfDaysForAMonth(10) * 2;
        RosterParametrization rosterParametrization = new RosterParametrization();
        List<Skill> skillList = createSkillList(skillListSize);
        List<Spot> spotList = createSpotList(spotListSize, skillList);
        List<TimeSlot> timeSlotList = createTimeSlotList(timeSlotListSize, continuousPlanning);
        List<Employee> employeeList = createEmployeeList(employeeListSize, skillList, timeSlotList);
        List<ShiftAssignment> shiftAssignmentList = createShiftAssignmentList(spotList, timeSlotList, employeeList, continuousPlanning);
        return new Roster(rosterParametrization,
                skillList, spotList, timeSlotList, employeeList,
                shiftAssignmentList);
    }

    private List<Skill> createSkillList(int size) {
        List<Skill> skillList = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            String name = skillNameGenerator.generateNextValue();
            skillList.add(new Skill(name));
        }
        return skillList;
    }

    private List<Spot> createSpotList(int size, List<Skill> skillList) {
        List<Spot> spotList = new ArrayList<>(size);
        String name = "Tuas";
        if(numSpots == 28){
            name = "Woodlands";
        }
        
        spotList.add(new Spot(name + 0, new Skill("Mandarin")));
        spotList.add(new Spot(name + 1, new Skill("Malay")));
        spotList.add(new Spot(name + 2, new Skill("Tamil")));
        spotList.add(new Spot(name + 3, new Skill("fSearch")));
        for (int i = 4; i < size; i++) {
            spotList.add(new Spot(name + i, new Skill("Any")));
        }

        /* for (int i = 0; i < size; i++) {
         String name = spotNameGenerator.generateNextValue();
         spotList.add(new Spot(name, skillList.get(random.nextInt(skillList.size()))));
         }*/
        return spotList;
    }

    private List<TimeSlot> createTimeSlotList(int size, boolean continuousPlanning) {
        List<TimeSlot> timeSlotList = new ArrayList<>(size);
        LocalDateTime previousEndDateTime = LocalDateTime.of(2017, 12, 1, 8, 0);
        for (int i = 0; i < size; i++) {
            LocalDateTime startDateTime = previousEndDateTime;
            LocalDateTime endDateTime = startDateTime.plusHours(12);
            TimeSlot timeSlot = new TimeSlot(startDateTime, endDateTime);
            if (continuousPlanning && i < size / 2) {
                if (i < size / 4) {
                    timeSlot.setTimeSlotState(TimeSlotState.HISTORY);
                } else {
                    timeSlot.setTimeSlotState(TimeSlotState.TENTATIVE);
                }
            } else {
                timeSlot.setTimeSlotState(TimeSlotState.DRAFT);
            }
            timeSlotList.add(timeSlot);
            previousEndDateTime = endDateTime;
        }
        return timeSlotList;
    }
    /*
     private List<Employee> createEmployeeList(int size, List<Skill> generalSkillList, List<TimeSlot> timeSlotList) {
     List<Employee> employeeList = new ArrayList<>(size);
     for (int i = 0; i < size; i++) {
     String name = employeeNameGenerator.generateNextValue();
     LinkedHashSet<Skill> skillSet = new LinkedHashSet<>(extractRandomSubList(generalSkillList, 1.0));
     Employee employee = new Employee(name, skillSet);
     Set<TimeSlot> unavailableTimeSlotSet = new LinkedHashSet<>(extractRandomSubList(timeSlotList, 0.2));
     employee.setUnavailableTimeSlotSet(unavailableTimeSlotSet);
     employeeList.add(employee);
     }
     return employeeList;
     }
     */

    private List<Employee> createEmployeeList(int size, List<Skill> generalSkillList, List<TimeSlot> timeSlotList) {
        List<Employee> employeeList = new ArrayList<>(size);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        //EmployeeController ec = new EmployeeController();
        EmployeeDAO ed = new EmployeeDAO();
        ArrayList<bean.Employee> empList = ed.getAllEmployees();
        
        //get reserve team
        ArrayList<bean.Employee> reserveEmpList = ed.getAllReserveEmployees();
        
        for (int i = 0; i < size; i++) {
            bean.Employee e = empList.get(i);
            LinkedHashSet<Skill> skillSet = new LinkedHashSet<>();
            skillSet.add(new Skill(e.getLanguageSpoken()));
            if (e.getGender().equals("Male")) {
                skillSet.add(new Skill("mSearch"));
            } else {
                skillSet.add(new Skill("fSearch"));
            }
            Employee employee = new Employee(e.getName(), skillSet, false);
            //Set<TimeSlot> unavailableTimeSlotSet = new LinkedHashSet<>();
            String nric = e.getNric();

            String format = "yyyy-MM-dd'T'HH:mm";
            DateFormat df = new SimpleDateFormat(format);
            Set<TimeSlot> unavailableTimeSlotSet = new LinkedHashSet<>();
            ArrayList<Time> tsList = ed.retrieveAllTimeSlot(nric);
            for (Time t : tsList) {
                Timestamp startTime = t.getStartTime();
                Timestamp endTime = t.getEndTime();
                LocalDateTime startTimeSlot = startTime.toLocalDateTime();
                LocalDateTime endTimeSlot = endTime.toLocalDateTime();
                int sTs = startTimeSlot.getDayOfYear();
                int eTs = endTimeSlot.getDayOfYear();
                int numLoops = eTs - sTs + 1;
                for (int j = 0; j < numLoops; j++) {
                    String startStr = sdf.format(startTime) + "T08:00";
                    String startStr2 = sdf.format(startTime) + "T20:00";
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(startTime);
                    cal.add(Calendar.DAY_OF_WEEK, 1);
                    startTime.setTime(cal.getTime().getTime());
                    String startStr3 = sdf.format(startTime) + "T08:00";
                    try {
                        Date startD = df.parse(startStr);
                        Date endD = df.parse(startStr2);
                        Date thirdD = df.parse(startStr3);
                        Timestamp startTs = new Timestamp(startD.getTime());
                        Timestamp endTs = new Timestamp(endD.getTime());
                        Timestamp thirdTs = new Timestamp(thirdD.getTime());
                        TimeSlot tee = new TimeSlot(startTs.toLocalDateTime(), endTs.toLocalDateTime());
                        TimeSlot tee2 = new TimeSlot(endTs.toLocalDateTime(),thirdTs.toLocalDateTime());
                        tee.setTimeSlotState(TimeSlotState.DRAFT);
                        tee2.setTimeSlotState(TimeSlotState.DRAFT);
                        unavailableTimeSlotSet.add(tee);
                        unavailableTimeSlotSet.add(tee2);
                    } catch (ParseException ex) {
                        Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //TimeSlot tee = new TimeSlot(startTimeSlot, endTimeSlot);
                //tee.setTimeSlotState(TimeSlotState.DRAFT);
                //unavailableTimeSlotSet.add(tee);
                //System.out.println(startTimeSlot.toString());
                //System.out.println(endTimeSlot.toString());
                /*
                Iterator<TimeSlot> iter = unavailableTimeSlotSet.iterator();
                while(iter.hasNext()){
                    TimeSlot tssss = iter.next();
                    System.out.println(tssss.getStartDateTime().toString()+" - "+tssss.getEndDateTime().toString());
                }
                */
            }

            //Set<TimeSlot> unavailableTimeSlotSet = new LinkedHashSet<>(extractRandomSubList(timeSlotList, 0.2));
            employee.setUnavailableTimeSlotSet(unavailableTimeSlotSet);
            employeeList.add(employee);
        }
        return employeeList;
    }

    private List<ShiftAssignment> createShiftAssignmentList(List<Spot> spotList, List<TimeSlot> timeSlotList,
            List<Employee> employeeList, boolean continuousPlanning) {
        List<ShiftAssignment> shiftAssignmentList = new ArrayList<>(spotList.size() * timeSlotList.size());
        for (Spot spot : spotList) {
//            boolean weekendEnabled = random.nextInt(10) < 8;
            boolean weekendEnabled = true;
            boolean nightEnabled = true;
//            boolean nightEnabled = weekendEnabled && random.nextInt(10) < 8;
            int timeSlotIndex = 0;
            for (TimeSlot timeSlot : timeSlotList) {
                DayOfWeek dayOfWeek = timeSlot.getStartDateTime().getDayOfWeek();
                if (!weekendEnabled && (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY)) {
                    timeSlotIndex++;
                    continue;
                }
                if (!nightEnabled && timeSlot.getStartDateTime().getHour() >= 20) {
                    timeSlotIndex++;
                    continue;
                }
                ShiftAssignment shiftAssignment = new ShiftAssignment(spot, timeSlot);
                if (continuousPlanning) {
                    if (timeSlotIndex < timeSlotList.size() / 2) {
                        List<Employee> availableEmployeeList = employeeList.stream()
                                .filter(employee -> !employee.getUnavailableTimeSlotSet().contains(timeSlot))
                                .collect(Collectors.toList());
                        Employee employee = availableEmployeeList.get(random.nextInt(availableEmployeeList.size()));
                        shiftAssignment.setEmployee(employee);
                        shiftAssignment.setLockedByUser(false);
                    }
                }
                shiftAssignmentList.add(shiftAssignment);
                timeSlotIndex++;
            }

        }
        return shiftAssignmentList;

    }

    private <E> List<E> extractRandomSubList(List<E> list, double maxRelativeSize) {
        List<E> subList = new ArrayList<>(list);
        Collections.shuffle(subList, random);
        // TODO List.subList() doesn't allow outer list to be garbage collected
        return subList.subList(0, random.nextInt((int) (list.size() * maxRelativeSize)) + 1);
    }

}
