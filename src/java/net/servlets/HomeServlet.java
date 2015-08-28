package net.servlets;

import forum.SingletonForum;
import forum.entity.Topic;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.StringTokenizer;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.database.DBConnection;

@WebServlet(name = "HomeServlet", urlPatterns = {"/home/*"})
public class HomeServlet extends HttpServlet {

    private DBConnection dbConn = DBConnection.getInstance();

    @Override
    public void destroy() {
        super.destroy(); //To change body of generated methods, choose Tools | Templates.
        DBConnection.destroy();
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

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
        response.setContentType("text/html;charset=UTF-8");
        SingletonForum forum = SingletonForum.getInstance();
        String url = request.getPathInfo();
        StringTokenizer tokens = new StringTokenizer(url, "/");

        try (PrintWriter out = response.getWriter()) {
            if (tokens.hasMoreTokens()) {
                int id = -1;
                try {
                    id = Integer.valueOf(tokens.nextToken());
                } catch (NumberFormatException ex) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                try {
                    Topic topic = dbConn.getTopicById(id);
                    request.setAttribute("topic", topic);
                    request.setAttribute("id", id);
                    request.getRequestDispatcher("/WEB-INF/jsp/pages/comment/comment.jsp").forward(request, response);
                } catch (SQLException ex) {
                    out.println(ex.getMessage());
                }

            } else {
                try {
                    request.setAttribute("topics", dbConn.getTopics());
                    //out.println("anyad");
                    request.getRequestDispatcher("/WEB-INF/jsp/pages/root/root.jsp").forward(request, response);
                } catch (SQLException ex) {
                    out.println(ex.getMessage());
                }
            }
        }
    }

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

        SingletonForum forum = SingletonForum.getInstance();
        String url = request.getPathInfo();
        StringTokenizer tokens = new StringTokenizer(url, "/");
        try {
            if (tokens.hasMoreTokens()) {
                int topicId = -1;
                try {
                    topicId = Integer.valueOf(tokens.nextToken());
                } catch (NumberFormatException ex) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                if ("OK".equals(dbConn.persistComment(topicId, request))) {
                    response.sendRedirect("/Forum/home/" + topicId);
                }
            } else {
                if ("OK".equals(dbConn.persistTopic(request))) {
                    response.sendRedirect("/Forum/home/");
                }
            }
        } catch (SQLException ex) {
            response.getWriter().println(ex.getMessage());
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
