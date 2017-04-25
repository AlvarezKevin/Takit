package me.kevindevelops.takit;

import java.io.Serializable;

/**
 * Created by Kevin on 4/24/2017.
 */

public class Post implements Serializable{
    private String username;
    private String subject;
    private String text;
    private String location;
    private String email;
    private String photoUrl;
    private String date;

    public Post() {

    }

    public Post(String username, String subject, String text, String location, String email, String photoUrl, String date) {
        this.username = username;
        this.subject = subject;
        this.text = text;
        this.location = location;
        this.email = email;
        this.photoUrl = photoUrl;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
