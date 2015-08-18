package entity;

import java.util.Date;

public class User {

    private int id;
    private String name; // nickname és/vagy teljes igazi név
    private String password; // hash érték + sózni is kell?
    private String email;
    private long regDate;
    // rollok is kellenek? meg kell különböztetni speciális jogkörrel rendelkező felhasználókat is? akkor guesteket (anonymous comment) is kell kezelni?
    
    public User(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
        regDate = new Date().getTime();
    }

    public int getId() {
        return id;
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
}
