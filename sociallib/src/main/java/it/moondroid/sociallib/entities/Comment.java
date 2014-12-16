package it.moondroid.sociallib.entities;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by marco.granatiero on 24/11/2014.
 */
@ParseClassName("Comment")
public class Comment extends ParseObject {

    public Comment() {
        // A default constructor is required.
    }

    public Comment(Post post, String text){
        setToPost(post);
        setFromUser(ParseUser.getCurrentUser());
        setText(text);
        setDate(new Date());
    }

    public Post getToPost() {
        return (Post) get("post");
    }

    public void setToPost(Post post) {
        put("post", post);
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
