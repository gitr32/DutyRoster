package servlet;

import controller.EmployeeController;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author User
 */
@WebServlet(name = "AddUnavailableTimeSlotServlet", urlPatterns = {"/AddUnavailableTimeSlotServlet"})
public class AddUnavailableTimeSlotServlet extends HttpServlet {


    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        EmployeeController ec = new EmployeeController();
        String nric = request.getParameter("nric");
        String startTimeSlot = request.getParameter("startTimeSlot");
        String endTimeSlot = request.getParameter("endTimeSlot");
        startTimeSlot+= "T08:00";
        endTimeSlot+= "T20:00";
        boolean b = ec.addTimeSlot(nric,startTimeSlot,endTimeSlot);
        if(b){
            System.out.println("Added Successfully");
        }else{
            System.out.println("Error!");
        }
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
