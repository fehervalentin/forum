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

    private final String _break = "<br/>";
    private final String nicknameField = "Készítő: <input type=\"text\" name=\"nickname\">";
    private final String titleField = "Topik neve: <input type=\"text\" name=\"title\">";
    private final String descriptionField = "Topik leírása: <input type=\"text\" name=\"description\" />";
    private final String contentField = "Tartalom: <input type=\"text\" name=\"content\">";
    private final String submitButton = "<input type=\"submit\" value=\"Submit\" />";
    private final String topicForm = ""
            + "<form action=\"/Forum/home/\" method=\"POST\">"
            + _break + nicknameField
            + _break + titleField
            + _break + descriptionField
            + _break + submitButton + "</form>";

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
        response.setContentType("text/html;charset=UTF-8");
        StringTokenizer tokens = new StringTokenizer(request.getPathInfo(), "/");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet HomeServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet HomeServlet at " + request.getContextPath() + "</h1>");
            out.println(topicForm);
            out.println("</body>");
            out.println("</html>");
        }
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
                printHeader(out, topic.getTitle());
                if (topic.getComments().size() < 1) {
                    out.println("<h1>Nincsenek még tárolt hozzászólások!</h1>");
                } else {
                    //out.println(topic.getComments().size());
                    for (Comment c : topic.getComments()) {
                        out.println(c.toString());
                        out.println("<br>");
                    }
                }
                out.println("<h1>Szólj hozzá te is!</h1>");
                out.println("<br>");
                out.println("<br>");
                String commentForm = "<form action=\"/Forum/home/" + id + "\" method=\"POST\">" + contentField + _break + submitButton + "</form>";
                out.println(commentForm);
            } else {
                /* TODO output your page here. You may use following sample code. */
                printHeader(out, "Forum");
                List<Topic> topics = forum.getTopics();
                if (topics.size() < 1) {
                    out.println("<h1>Nincsenek még tárolt topikok!</h1>");
                } else {
                    for (Topic topic : topics) {
                        out.println(topic.toString());
                        out.println("<br>");
                        out.println("<a href=\"/Forum/home/" + topic.getId() + "\">" + topic.getTitle() + "</a>");
                        out.println("<br>");
                    }
                }
                out.println("<h1>Hozz létre egy új topicot!</h1>");
                out.println("<br>");
                out.println("<br>");
                out.println(topicForm);
            }
            out.println();
            printFooter(out);
        }

        //processRequest(request, response);
    }

    private void printHeader(PrintWriter out, String title) {
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet HomeServlet</title>");
        out.println("</head>");
        out.println("<body>");
    }

    private void printFooter(PrintWriter out) {
        out.println("</body>");
        out.println("</html>");
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
            Topic t = forum.getTopics().get(id);
            String content = request.getParameter("content");
            t.addComment(new Comment(t, null, content, null));
            response.sendRedirect("/Forum/home/" + id);
        } else {
            String nickname = request.getParameter("nickname");
            String title = request.getParameter("title");
            User u = new User(nickname, "", "");
            String description = request.getParameter("description");
            Topic t = new Topic(title, description, u);
            SingletonForum.getInstance().addTopic(t);
            response.sendRedirect("/Forum/home/");
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
