package net.servlets;

import forum.SingletonForum;
import forum.entity.Comment;
import forum.entity.Topic;
import forum.entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.StringTokenizer;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "HomeServlet", urlPatterns = {"/home/*"})
public class HomeServlet extends HttpServlet {
    
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
                Topic topic = forum.getTopics().get(id);
                request.setAttribute("topic", topic);
                request.setAttribute("id", id);
                request.getRequestDispatcher("/WEB-INF/jsp/pages/comment/comment.jsp").forward(request, response);
            } else {
                request.setAttribute("topics", forum.getTopics());
                request.getRequestDispatcher("/WEB-INF/jsp/pages/root/root.jsp").forward(request, response);
            }
        }

        //processRequest(request, response);
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

        if (tokens.hasMoreTokens()) {
            int id = -1;
            try {
                id = Integer.valueOf(tokens.nextToken());
            } catch (NumberFormatException ex) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            if ("OK".equals(persistComment(id, request))) {
                response.sendRedirect("/Forum/home/" + id);
            }
        } else {
            if ("OK".equals(persistTopic(request))) {
                response.sendRedirect("/Forum/home/");
            }
        }
    }

    private String persistComment(int id, HttpServletRequest request) {
        Topic t = SingletonForum.getInstance().getTopics().get(id);
        String content = request.getParameter("content");
        t.addComment(new Comment(t, null, content, null));
        return "OK";
    }

    private String persistTopic(HttpServletRequest request) {
        String nickname = request.getParameter("nickname");
        String title = request.getParameter("title");
        User u = new User(nickname, "", "");
        String description = request.getParameter("description");
        Topic t = new Topic(title, description, u);
        SingletonForum.getInstance().addTopic(t);
        return "OK";
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
