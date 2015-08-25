package forum;

import forum.entity.Topic;
import forum.entity.User;
import java.util.ArrayList;
import java.util.List;

public class SingletonForum {
    private static SingletonForum instance = null;
    
    private static List<Topic> topics;
    private static List<User> users;
    
    private SingletonForum(){}
    
    public static SingletonForum getInstance() { // pattern szerint nincs lazy load, kiveszem, ha kell...
        if (null == instance) {
            instance = new SingletonForum();
            topics = new ArrayList<>();
            users = new ArrayList<>();
        }
        return instance;
    }
    
    public List<Topic> getTopics() {
        return topics;
    }
    
    public List<User> getUsers() {
        return users;
    }
    
    public void addTopic(Topic t) {
        topics.add(t);
    }
    
    public void addUser(User u) {
        users.add(u);
    }
    
    public String getInfo() {
        return "Users: " + users + System.lineSeparator() + 
               "Topics: " + topics;
    }
}
