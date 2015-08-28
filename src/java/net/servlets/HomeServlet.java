package net.servlets;

import forum.SingletonForum;
import forum.entity.Comment;
import forum.entity.Topic;
import forum.entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "HomeServlet", urlPatterns = {"/home/*"})
public class HomeServlet extends HttpServlet {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/forum";

    static final String USER = "root";
    static final String PASS = "";

    private Connection conn = null;

    @Override
    public void init() throws ServletException {
        super.init(); //To change body of generated methods, choose Tools | Templates.
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        super.destroy(); //To change body of generated methods, choose Tools | Templates.
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(HomeServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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
                    Topic topic = getTopicById(id);
                    request.setAttribute("topic", topic);
                    request.setAttribute("id", id);
                    request.getRequestDispatcher("/WEB-INF/jsp/pages/comment/comment.jsp").forward(request, response);
                } catch (SQLException ex) {
                    out.println(ex.getMessage());
                }

            } else {
                try {
                    request.setAttribute("topics", getTopics());
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
                if ("OK".equals(persistComment(topicId, request))) {
                    response.sendRedirect("/Forum/home/" + topicId);
                }
            } else {
                if ("OK".equals(persistTopic(request))) {
                    response.sendRedirect("/Forum/home/");
                }
            }
        } catch (SQLException ex) {
            response.getWriter().println(ex.getMessage());
        }
    }

    private List<Topic> getTopics() throws SQLException {
        List<Topic> topics = new ArrayList<>();
        PreparedStatement prep = conn.prepareStatement("select * from topics");
        ResultSet rs = prep.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String title = rs.getString("title");
            String description = rs.getString("description");
            long date = rs.getLong("date");
            int user_id = rs.getInt("user_id");
            User u = getUserById(user_id);
            Topic t = new Topic(title, description, u, date);
            t.setId(id);
            topics.add(t);
        }
        return topics;
    }

    private User getUserById(int id) throws SQLException {
        PreparedStatement prep = conn.prepareStatement("select * from users where id = ?");
        prep.setInt(1, id);
        ResultSet rs = prep.executeQuery();
        if (rs.next()) {
            String username = rs.getString("username");
            String password = rs.getString("password");
            String email = rs.getString("email");
            long regDate = rs.getLong("regDate");
            User u = new User(username, password, email, regDate);
            u.setId(id);
            return u;
        }
        return null;
    }
    
    private List<Comment> getCommentsFromTopic(int topicId) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        PreparedStatement prep = conn.prepareStatement("select * from comments where topic_id = ?");
        prep.setInt(1, topicId);
        ResultSet rs = prep.executeQuery();
        while (rs.next()) {
            int commentId = rs.getInt("id");
            int userId = rs.getInt("user_id");
            String content = rs.getString("content");
            long date = rs.getLong("date");
            Comment c = new Comment(getUserById(userId), content, null, date);
            c.setId(0);
            comments.add(c);
        }
        prep.close();
        return comments;
    }

    private Topic getTopicById(int id) throws SQLException {
        PreparedStatement prep = conn.prepareStatement("select * from topics where id = ?");
        prep.setInt(1, id);
        ResultSet rs = prep.executeQuery();
        if (rs.next()) {
            String title = rs.getString("title");
            String description = rs.getString("description");
            long date = rs.getLong("date");
            Topic t = new Topic(title, description, getUserById(1), date);
            t.setId(id);
            t.addComments(getCommentsFromTopic(id));
            prep.close();
            return t;
        }
        prep.close();
        return null;
    }

    private String persistComment(int topicId, HttpServletRequest request) throws SQLException {
        String content = request.getParameter("content");
        if (null != conn) {
            PreparedStatement prep = conn.prepareStatement("insert into comments (topic_id, user_id, content, date) values(?, 1, ?, ?)");
            prep.setInt(1, topicId);
            prep.setString(2, content);
            prep.setLong(3, new Date().getTime());
            prep.executeUpdate();
            prep.close();
        }
        return "OK";
    }

    private String persistTopic(HttpServletRequest request) throws SQLException {
        if (null != conn) {
            String nickname = request.getParameter("nickname");
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            PreparedStatement prep = conn.prepareStatement("insert into topics (title, description, date, user_id) values(?, ?, ?, ?)");
            prep.setString(1, title);
            prep.setString(2, description);
            prep.setLong(3, new Date().getTime());
            prep.setInt(4, 1);
            prep.executeUpdate();
            prep.close();
        }
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
