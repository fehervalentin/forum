package forum.entity;

import java.util.Date;

public class User {

    private int id;
    private String name; // nickname és/vagy teljes igazi név
    private String password; // hash érték + sózni is kell?
    private String email;
    private long regDate;
    // rollok is kellenek? meg kell különböztetni speciális jogkörrel rendelkező felhasználókat is? akkor guesteket (anonymous comment) is kell kezelni?
    
    public User(String name, String password, String email, long regDate) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.regDate = regDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public long getRegDate() {
        return regDate;
    }
    
    // setter szerintem egyik adattaghoz sem kell

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name=" + name + ", password=" + password + ", email=" + email + ", regDate=" + regDate + '}';
    }
}
