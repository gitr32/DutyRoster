package servlet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.optaplanner.training.workerrostering.app.WorkerRosteringApp;
import org.optaplanner.training.workerrostering.persistence.WorkerRosteringGenerator;

/**
 *
 * @author User
 */
@WebServlet(urlPatterns = {"/GetScheduleServlet"})
public class GetScheduleServlet extends HttpServlet {

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

        //PrintWriter out = response.getWriter();
        //Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().create();
        String place = request.getParameter("area");
        String numberOfDays = request.getParameter("numDays");
        String filePath = WorkerRosteringApp.generate(Integer.parseInt(numberOfDays), place);

        FileInputStream inStream = new FileInputStream(filePath);
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition",
                "attachment;filename=roster.xls");

        // obtains response's output stream
        OutputStream outStream = response.getOutputStream();

        byte[] buffer = new byte[4096];
        int bytesRead = -1;

        while ((bytesRead = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, bytesRead);
        }

        inStream.close();
        outStream.close();
    }
}