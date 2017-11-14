package bean;

import java.util.Random;

/**
 *
 * @author Alex.Tiang
 */
public class Employee {
    
    private String nric;
    private String name;
    private String race;
    private String gender;
    private String languageSpoken;
    private long points;
    private boolean isReserve;
    private String shiftType;

    public Employee(String nric, String name, String race, String gender, String languageSpoken, long points, boolean isReserve) {
        this.nric = nric;
        this.name = name;
        this.race = race;
        this.gender = gender;
        this.languageSpoken = languageSpoken;
        this.points = points;
        this.isReserve = isReserve;
        Random d = new Random();
        double value = d.nextDouble();
        if(value < 0.05){
            shiftType = "Morning";
        }else if(value < 0.10){
            shiftType = "Night";
        }else{
            shiftType = "Any";
        }
    }

    public String getShift(){
        return shiftType;
    }
    
    public String getNric() {
        return nric;
    }

    public void setNric(String nric) {
        this.nric = nric;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLanguageSpoken() {
        return languageSpoken;
    }

    public void setLanguageSpoken(String languageSpoken) {
        this.languageSpoken = languageSpoken;
    }
    
    public long getPoints(){
        return points;
    }
    
    public void setPoints(long points){
        this.points = points;
    }
    
    public boolean getIsReserve(){
        return isReserve;
    }
    
    public void setIsReserve(boolean isReserve){
        this.isReserve = isReserve;
    }
}