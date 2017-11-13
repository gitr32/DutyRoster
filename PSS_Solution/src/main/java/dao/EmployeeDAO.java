package dao;

import bean.Employee;
import bean.Time;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import utility.ConnectionManager;

/**
 * This class is used to manage employees
 *
 * @author Alex.Tiang
 */
public class EmployeeDAO {

    public ArrayList<Employee> getAllEmployees() {

        ArrayList<Employee> employeeList = new ArrayList<>();

        Connection c = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        //retrieving of data
        c = ConnectionManager.getConnection();

        try {
            stmt = c.prepareStatement("SELECT * FROM public.employees e, public.employeespoints ep where e.nric = ep.nric order by points asc;");
            rs = stmt.executeQuery();
            while (rs.next()) {
                String nric = rs.getString("nric");
                String name = rs.getString("name");
                String race = rs.getString("race");
                String gender = rs.getString("gender");
                String languageSpoken = rs.getString("languagespoken");
                long points = rs.getLong("points");
                Employee e = new Employee(nric, name, race, gender, languageSpoken, points,false);
                employeeList.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                ConnectionManager.closeAll(rs, stmt, c);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return employeeList;
    }
    
    public ArrayList<Employee> getAllReserveEmployees() {

        ArrayList<Employee> employeeList = new ArrayList<>();

        Connection c = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        //retrieving of data
        c = ConnectionManager.getConnection();

        try {
            stmt = c.prepareStatement("SELECT * FROM public.reserveemployees;");
            rs = stmt.executeQuery();
            while (rs.next()) {
                String nric = rs.getString("nric");
                String name = rs.getString("name");
                String race = rs.getString("race");
                String gender = rs.getString("gender");
                String languageSpoken = rs.getString("languagespoken");
                Employee e = new Employee(nric, name, race, gender, languageSpoken,0,true);
                employeeList.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                ConnectionManager.closeAll(rs, stmt, c);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return employeeList;
    }
    
    public ArrayList<Employee> getLanguageSpokenTeam(String language, String gen) {

        //Mandarin, Malay, Tamil
        ArrayList<Employee> languageSpokenList = new ArrayList<>();

        Connection c = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        //retrieving of data
        c = ConnectionManager.getConnection();

        try {
            stmt = c.prepareStatement("SELECT * FROM public.employees e, public.employeespoints ep where e.nric = ep.nric and"
                    + " languagespoken = ? and gender = ? order by points asc;");
            stmt.setString(1, language);
            stmt.setString(2, gen);
            rs = stmt.executeQuery();
            while (rs.next()) {
                String nric = rs.getString("nric");
                String name = rs.getString("name");
                String race = rs.getString("race");
                String gender = rs.getString("gender");
                String languageSpoken = rs.getString("languagespoken");
                long points = rs.getLong("points");
                Employee e = new Employee(nric, name, race, gender, languageSpoken, points,false);
                languageSpokenList.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                ConnectionManager.closeAll(rs, stmt, c);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return languageSpokenList;
    }

    public ArrayList<Time> retrieveAllTimeSlot(String nric) {
        ArrayList<Time> timestampList = new ArrayList<>();

        Connection c = null;
        PreparedStatement stmt = null;
        c = ConnectionManager.getConnection();
        ResultSet rs = null;
        if (c != null) {
            try {
                stmt = c.prepareStatement("select * from public.employeestimeslot where nric = ?;");
                stmt.setString(1, nric);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    java.sql.Timestamp startDate = rs.getTimestamp("starttimeslot");
                    java.sql.Timestamp endDate = rs.getTimestamp("endtimeslot");
                    timestampList.add(new Time(nric, startDate, endDate));
                }
            } catch (SQLException e) {
                return null;
            } finally {
                try {
                    ConnectionManager.close(stmt, c);
                } catch (SQLException ex) {
                    return null;
                }
            }
        }
        return timestampList;
    }

    public boolean addTimeSlot(String nric, java.sql.Timestamp startDate, java.sql.Timestamp endDate) {

        Connection c = null;
        PreparedStatement stmt = null;
        c = ConnectionManager.getConnection();
        if (c != null) {
            try {
                stmt = c.prepareStatement("INSERT INTO public.employeestimeslot (nric,starttimeslot,endtimeslot) VALUES (?,?,?);");
                stmt.setString(1, nric);
                stmt.setTimestamp(2, startDate);
                stmt.setTimestamp(3, endDate);
                return stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                return false;
            } finally {
                try {
                    ConnectionManager.close(stmt, c);
                } catch (SQLException ex) {
                    return false;
                }
            }
        }
        return false;
    }
    
    public boolean deleteTimeSlot(String nric, Timestamp startTimeSlot, Timestamp endTimeSlot) {
        Connection c = null;
        PreparedStatement stmt = null;
        c = ConnectionManager.getConnection();
        if (c != null) {
            try {
                stmt = c.prepareStatement("DELETE FROM public.employeestimeslot where nric = ? "
                        + "and starttimeslot = ? and endtimeslot = ?;");
                stmt.setString(1, nric);
                stmt.setTimestamp(2, startTimeSlot);
                stmt.setTimestamp(3, endTimeSlot);
                return stmt.executeUpdate() > 0;
            } catch (SQLException e) {
                return false;
            } finally {
                try {
                    ConnectionManager.close(stmt, c);
                } catch (SQLException ex) {
                    return false;
                }
            }
        }
        return false;
    }
}
