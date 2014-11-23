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

@ParseClassName("Follow")
public class Follow extends ParseObject {

    public Follow() {
        // A default constructor is required.
    }

    public Follow (ParseUser to){

        this(ParseUser.getCurrentUser(), to, new Date());
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


    public void getUsersImFollowing(final FindCallback<ParseObject> callback){
        // set up the query on the Follow table
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Follow");
        query.whereEqualTo("from", ParseUser.getCurrentUser());

        // execute the query
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List <ParseObject> followList, ParseException e) {
                callback.done(followList, e);
            }
        });
    }

    public void getUsersFollowingMe (final FindCallback<ParseObject> callback){
        // set up the query on the Follow table
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Follow");
        query.whereEqualTo("to", ParseUser.getCurrentUser());

        // execute the query
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> followList, ParseException e) {
                callback.done(followList, e);
            }
        });
    }
}
