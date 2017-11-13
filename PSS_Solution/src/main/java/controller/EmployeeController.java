package controller;

import bean.Employee;
import bean.Time;
import dao.EmployeeDAO;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alex.Tiang
 */
public class EmployeeController {

    private EmployeeDAO ed;

    public EmployeeController() {
        ed = new EmployeeDAO();
    }

    public LinkedHashMap<String, ArrayList<Employee>> getSchedule() {

        //each morning - 15 male chinese, 3 male malay, 2 male indian, 11 female chinese, 2 female malay, 1 female indian
        //each night - 15 male chinese, 3 male malay, 2 male indian, 11 female chinese, 2 female malay, 1 female indian
        int month = 9; //10 stands for november
        int year = 2018;
        int numOfDaysToPlan = getNumberOfDaysForAMonth(month);

        //get squad based on points
        ArrayList<Employee> chineseMale = getChineseGuySquad();
        ArrayList<Employee> malayMale = getMalayGuySquad();
        ArrayList<Employee> indianMale = getIndianGuySquad();
        ArrayList<Employee> chineseFemale = getChineseGirlSquad();
        ArrayList<Employee> malayFemale = getMalayGirlSquad();
        ArrayList<Employee> indianFemale = getIndianGirlSquad();

        //after getting the squads, assign 2 days and give them off 2 days
        ArrayList<Employee> team1Morning = new ArrayList<>();
        ArrayList<Employee> team1Night = new ArrayList<>();

        //team 1 morning
        for (int i = 0; i < 15; i++) {
            team1Morning.add(chineseMale.get(i));
            team1Night.add(chineseMale.get(i + 15));
            if (i < 11) {
                team1Morning.add(chineseFemale.get(i));
                team1Night.add(chineseFemale.get(i + 11));
            }
            if (i < 3) {
                team1Morning.add(malayMale.get(i));
                team1Night.add(malayMale.get(i + 3));
            }
            if (i < 2) {
                team1Morning.add(indianMale.get(i));
                team1Night.add(indianMale.get(i + 2));
                team1Morning.add(malayFemale.get(i));
                team1Night.add(malayFemale.get(i + 2));
            }
            if (i < 1) {
                team1Morning.add(indianFemale.get(i));
                team1Night.add(indianFemale.get(i + 1));
            }
        }

        LinkedHashMap<String, ArrayList<Employee>> scheduleMap = new LinkedHashMap<>();

        int count = 0;
        int count2 = 0;
        for (int i = 1; i <= numOfDaysToPlan; i++) {
            String d = (i) + "-" + (month + 1) + "-" + year;
            if (count == 2) {
                //team 2
                count2++;
            } else {
                //team 1
                scheduleMap.put(d + " (Morning)", team1Morning);
                scheduleMap.put(d + " (Night", team1Night);
                count++;
            }
            if (count2 == 2) {
                count = 0;
                count2 = 0;
            }
        }

        //get another team, assign the alternate 2 days and give them off 2 days
        ArrayList<Employee> team2Morning = new ArrayList<>();
        ArrayList<Employee> team2Night = new ArrayList<>();

        //update the points
        //save the information into the database
        return scheduleMap;
    }
    
    public ArrayList<Time> retrieveAllTimeSlot(String nric){
        
        ArrayList<Time> timeSlotList = ed.retrieveAllTimeSlot(nric);
        return timeSlotList;
        
    }
    
    public boolean deleteTimeSlot(String nric, String startTimeSlot, String endTimeSlot){
        
        //2017-11-14T20:00
        
        //String startDate = "2017-11-14T20:00";
        //String endDate = "2017-11-16T20:00";
        String format = "yyyy-MM-dd'T'HH:mm";
        //nric = "S1212121e";
        DateFormat df = new SimpleDateFormat(format);
        try {
            Date startD = df.parse(startTimeSlot);
            Date endD = df.parse(endTimeSlot);
            Timestamp startTs = new Timestamp(startD.getTime());
            Timestamp endTs = new Timestamp(endD.getTime());
            ed.deleteTimeSlot(nric, startTs, endTs);
        } catch (ParseException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    public ArrayList<Time> displayUnavailableTimeSlot(String nric){
        
        ArrayList<Time> timeSlot = ed.retrieveAllTimeSlot(nric);
        return timeSlot;
        
    }
    
    public boolean addTimeSlot(String nric, String startTimeSlot, String endTimeSlot){
        
        //2017-11-14T20:00
        
        //String startDate = "2017-11-14T20:00";
        //String endDate = "2017-11-16T20:00";
        String format = "yyyy-MM-dd'T'HH:mm";
        //nric = "S1212121e";
        DateFormat df = new SimpleDateFormat(format);
        try {
            Date startD = df.parse(startTimeSlot);
            Date endD = df.parse(endTimeSlot);
            Timestamp startTs = new Timestamp(startD.getTime());
            Timestamp endTs = new Timestamp(endD.getTime());
            ed.addTimeSlot(nric, startTs, endTs);
        } catch (ParseException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    
    private int getNumberOfDaysForAMonth(int month) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, month);
        return c.getActualMaximum(Calendar.DATE);
    }

    public ArrayList<Employee> getEmployees() {
        return ed.getAllEmployees();
    }
    
    public ArrayList<Employee> getReserveEmployees(){
        return ed.getAllReserveEmployees();
    }

    public ArrayList<Employee> getMalayGirlSquad() {
        return ed.getLanguageSpokenTeam("Malay", "Female");
    }

    public ArrayList<Employee> getChineseGirlSquad() {
        return ed.getLanguageSpokenTeam("Mandarin", "Female");
    }

    public ArrayList<Employee> getIndianGirlSquad() {
        return ed.getLanguageSpokenTeam("Tamil", "Female");
    }

    public ArrayList<Employee> getMalayGuySquad() {
        return ed.getLanguageSpokenTeam("Malay", "Male");
    }

    public ArrayList<Employee> getIndianGuySquad() {
        return ed.getLanguageSpokenTeam("Tamil", "Male");
    }

    public ArrayList<Employee> getChineseGuySquad() {
        return ed.getLanguageSpokenTeam("Mandarin", "Male");
    }
    
    public static HashMap<String, Integer> getSpotsVariables() {

        HashMap<String, Integer> spotsMap = new HashMap<String, Integer>();
        String location = "D:\\location.csv";
        String line = "";
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(location))) {
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] variable = line.split(cvsSplitBy);
                String header = variable[0];
                String value = variable[1];
                if (header.equals("tuas")) {
                    spotsMap.put("tuas", Integer.parseInt(value));
                } else if (header.equals("woodlands")) {
                    spotsMap.put("woodlands", Integer.parseInt(value));
                }
            }
        } catch (IOException e) {
            System.out.println("get spots");
        }
        return spotsMap;
    }
}
