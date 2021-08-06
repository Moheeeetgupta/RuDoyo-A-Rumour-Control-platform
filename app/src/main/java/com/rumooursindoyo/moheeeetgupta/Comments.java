package com.rumooursindoyo.moheeeetgupta;
import java.util.Date;

/**
 * This is java class which contains every field to show in omments
 * when user comment or review to a particular post
 */
public class Comments {

    private String message, user_id;
    private Date timestamp; // on which date reviews are posted on a particular post
    public  Comments(){

    }

    public Comments(String message, String user_id, Date timestamp) {
        this.message = message;
        this.user_id = user_id;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
