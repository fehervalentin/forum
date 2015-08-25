package forum.entity;

import java.util.Date;

public class Comment {
    private int id;
    private Topic topic;
    private User user;
    private String content;
    private long date;
    private Comment reply; // melyik hsz-re érkezett válasz(=ez a hsz) lehet null is, ilyenkor egy egyszerű új hsz érkezett
    
    public Comment(Topic topic, User user, String content, Comment reply) {
        this.topic = topic;
        this.user = user;
        this.content = content;
        this.reply = reply;
        date = new Date().getTime();
    }

    public int getId() {
        return id;
    }

    public Topic getTopic() {
        return topic;
    }

    public User getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }

    public long getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Comment{" + "id=" + id + ", topic=" + topic + ", user=" + user + ", content=" + content + ", date=" + date + ", reply=" + reply + '}';
    }
}
