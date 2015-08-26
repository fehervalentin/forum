package forum.entity;

public class Comment {
    private int id;
    private User user;
    private String content;
    private long date;
    private Comment reply; // melyik hsz-re érkezett válasz(=ez a hsz) lehet null is, ilyenkor egy egyszerű új hsz érkezett
    
    public Comment(User user, String content, Comment reply, long date) {
        this.user = user;
        this.content = content;
        this.reply = reply;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return "Comment{" + "id=" + id + ", user=" + user + ", content=" + content + ", date=" + date + '}';
    }
}
