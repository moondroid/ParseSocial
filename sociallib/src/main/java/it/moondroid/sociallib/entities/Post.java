package it.moondroid.sociallib.entities;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by marco.granatiero on 24/11/2014.
 */
@ParseClassName("Post")
public class Post extends ParseObject {

    public Post() {
        // A default constructor is required.
    }

    public Post(String text){
        setFromUser(ParseUser.getCurrentUser());
        setText(text);
        setDate(new Date());
    }

    public String getText(){
        return getString("text");
    }

    public void setText(String text){
        put("text", text);
    }

    public ParseUser getFromUser() {
        return getParseUser("from");
    }

    public void setFromUser(ParseUser user) {
        put("from", user);
    }

    public Date getDate() {
        return getDate("date");
    }

    public void setDate(Date date) {
        put("date", date);
    }
}
