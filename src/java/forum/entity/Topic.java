package forum.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Topic {
    
    private int id;
    private String title;
    private String description;
    private long date;
    private User user;
    private List<Topic> subTopics; // Composite pattern
    private List<Comment> comments;
    
    public Topic(String title, String description, User user, long date) {
        this.title = title;
        this.description = description;
        this.user = user;
        this.date = date;
        subTopics = new ArrayList<>();
        comments = new ArrayList<>();
    }

    public int getId() { // setter nem kell, valamilyen automatizmus fogja generálni
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() { // vajon kell setter? később is meg lehessen változtatni a címet? vagy legyen setter, de csak mondjuk adminok tudják hívni? jogosultságok?! vagy közvetlenül db-ben kelljen hozzá turkálni?
        return title;
    }

    public String getDescription() { // ld. title
        return description;
    }

    public long getDate() { // ehhez nem kell setter (szerintem)
        return date;
    }

    public User getUser() { // ehhez sem kell setter (szerintem)
        return user;
    }

    public List<Topic> getSubTopics() {
        return subTopics; // közvetlen referenciát adok vissza...? pattern szerint igen, megkérdőjelezném!
    }
    
    public void addSubTopic(Topic t) { // a Composite patternben add/remove szerepel, ugyanakkor a kommentek miatt az eljárás nevét ki kellett (illett) egészítnem
        subTopics.add(t);
    }
    
    public void removeSubTopic(Topic t) {
        subTopics.remove(t);
    }
    
    public List<Comment> getComments() {
        return comments;
    }
    
    public void addComment(Comment c) {
        comments.add(c);
    }
    
    public void addComments(List<Comment> comments) {
        for (Comment comment : comments) {
            this.comments.add(comment);
        }
    }
    
    public void removeComment(Comment c) {
        comments.remove(c);
    }

    @Override
    public String toString() {
        return "Topic{" + "id=" + id + ", title=" + title + ", description=" + description + ", timestamp=" + date + ", user=" + user + ", subTopics=" + subTopics + ", comments=" + comments + '}';
    }
}
