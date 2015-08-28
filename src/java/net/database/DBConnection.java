package net.database;

import forum.entity.Comment;
import forum.entity.Topic;
import forum.entity.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;

public class DBConnection {

    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/forum";
    private static final String USER = "root";
    private static final String PASS = "";
    private static Connection conn = null;

    private static DBConnection instance = null;

    private DBConnection() {
    }

    public static DBConnection getInstance() {
        try {
            if (null == instance) {
                instance = new DBConnection();
                Class.forName(JDBC_DRIVER);
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        return instance;
    }

    public List<Topic> getTopics() throws SQLException {
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

    public User getUserById(int id) throws SQLException {
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

    public List<Comment> getCommentsFromTopic(int topicId) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        try (PreparedStatement prep = conn.prepareStatement("select * from comments where topic_id = ?")) {
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
        }
        return comments;
    }

    public Topic getTopicById(int id) throws SQLException {
        try (PreparedStatement prep = conn.prepareStatement("select * from topics where id = ?")) {
            prep.setInt(1, id);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                String title = rs.getString("title");
                String description = rs.getString("description");
                long date = rs.getLong("date");
                Topic t = new Topic(title, description, getUserById(1), date);
                t.setId(id);
                t.addComments(getCommentsFromTopic(id));
                return t;
            }
        }
        return null;
    }

    public String persistComment(int topicId, HttpServletRequest request) throws SQLException {
        String content = request.getParameter("content");
        if (null != conn) {
            try (PreparedStatement prep = conn.prepareStatement("insert into comments (topic_id, user_id, content, date) values(?, 1, ?, ?)")) {
                prep.setInt(1, topicId);
                prep.setString(2, content);
                prep.setLong(3, new Date().getTime());
                prep.executeUpdate();
            }
        }
        return "OK";
    }

    public String persistTopic(HttpServletRequest request) throws SQLException {
        if (null != conn) {
            String nickname = request.getParameter("nickname");
            String title = request.getParameter("title");
            String description = request.getParameter("description");
            try (PreparedStatement prep = conn.prepareStatement("insert into topics (title, description, date, user_id) values(?, ?, ?, ?)")) {
                prep.setString(1, title);
                prep.setString(2, description);
                prep.setLong(3, new Date().getTime());
                prep.setInt(4, 1);
                prep.executeUpdate();
            }
        }
        return "OK";
    }

    public static void destroy() {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
