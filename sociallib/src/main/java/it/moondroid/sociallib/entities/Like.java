package it.moondroid.sociallib.entities;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

/**
 * Created by Marco on 23/11/2014.
 */

/*
 * An extension of ParseObject that makes
 * it more convenient to access information
 * about users following other users (many-to-many relationship)
 */

@ParseClassName("Like")
public class Like extends ParseObject {

    public Like() {
        // A default constructor is required.
    }

    public Like(Post to){

        setFromUser(ParseUser.getCurrentUser());
        setToPost(to);
        setDate(new Date());

    }


    public ParseUser getFromUser() {
        return getParseUser("from");
    }

    public void setFromUser(ParseUser user) {
        put("from", user);
    }


    public Post getToPost() {
        return (Post) get("post");
    }

    public void setToPost(Post post) {
        put("post", post);
    }


    public Date getDate() {
        return getDate("date");
    }

    public void setDate(Date date) {
        put("date", date);
    }


}
