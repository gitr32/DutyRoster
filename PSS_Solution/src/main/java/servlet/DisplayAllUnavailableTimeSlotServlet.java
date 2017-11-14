/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlet;

import bean.JsonTime;
import bean.Time;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controller.EmployeeController;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author User
 */
@WebServlet(name = "DisplayAllUnavailableTimeSlotServlet", urlPatterns = {"/DisplayAllUnavailableTimeSlotServlet"})
public class DisplayAllUnavailableTimeSlotServlet extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");

        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        PrintWriter out = response.getWriter();
        
        EmployeeController ec = new EmployeeController();
        String nric = request.getParameter("nric");
        ArrayList<Time> timeSlot = ec.displayUnavailableTimeSlot(nric);
        ArrayList<JsonTime> returnTimeSlot = new ArrayList<>();
        for(Time t : timeSlot){
            String s = t.getStartTime().toLocalDateTime().toString().substring(0,t.getStartTime().toLocalDateTime().toString().indexOf("T"));
            String e = t.getEndTime().toLocalDateTime().toString().substring(0,t.getEndTime().toLocalDateTime().toString().indexOf("T"));
            returnTimeSlot.add(new JsonTime(nric,s,e));
        }
        out.println(gson.toJson(returnTimeSlot));
        
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
