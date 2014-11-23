package it.moondroid.sociallib.entities;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by Marco on 23/11/2014.
 */

/*
 * An extension of ParseObject that makes
 * it more convenient to access information
 * about users following other users (many-to-many relationship)
 */

@ParseClassName("Follow")
public class Follow extends ParseObject {

    public Follow() {
        // A default constructor is required.
    }

    public Follow(ParseUser from, ParseUser to){

        this(from, to, new Date());
    }

    public Follow(ParseUser from, ParseUser to, Date date){
        setFromUser(from);
        setToUser(to);
        setDate(date);
    }

    public ParseUser getFromUser() {
        return getParseUser("from");
    }

    public void setFromUser(ParseUser user) {
        put("from", user);
    }


    public ParseUser getToUser() {
        return getParseUser("to");
    }

    public void setToUser(ParseUser user) {
        put("to", user);
    }


    public Date getDate() {
        return getDate("date");
    }

    public void setDate(Date date) {
        put("date", date);
    }


}
