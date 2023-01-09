package me.oussa.ensaschat.model;

import java.io.Serializable;

public class User implements Serializable {

    public static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String username;
    private String password;
    private String status;

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public int getID() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}