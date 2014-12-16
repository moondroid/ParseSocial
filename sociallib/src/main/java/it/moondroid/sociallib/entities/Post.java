package it.moondroid.sociallib.entities;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by marco.granatiero on 24/11/2014.
 */
@ParseClassName("Post")
public class Post extends ParseObject {

    private Integer numLikes = null;
    private Boolean isLikedByMe = null;


    public Post() {
        // A default constructor is required.
    }

    public Post(String text){
        setFromUser(ParseUser.getCurrentUser());
        setText(text);
        setDate(new Date());
        put("num_comments", 0);
        put("num_likes", 0);
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

    public int getNumComments(){
        return getNumber("num_comments").intValue();
    }

    public int getNumLikes(){
        if(numLikes == null){
            return getNumber("num_likes").intValue();
        }else{
            return numLikes;
        }
    }

    public void setNumLikes(int numLikes){
        this.numLikes = numLikes;
    }

    public ArrayList<ParseUser> getUserLikes(){
        return  (ArrayList<ParseUser>) get("user_array_likes");
    }

    public boolean isLikedByMe(){
        if(isLikedByMe==null){
            ArrayList<ParseUser> users = getUserLikes();
            String currentUserId = ParseUser.getCurrentUser().getObjectId();
            for(ParseUser user : users){
                if (user.getObjectId().equals(currentUserId)){
                    return true;
                }
            }
            return false;
        }else {
            return isLikedByMe;
        }

    }

    public void setIsLikedByMe(boolean isLikedByMe){
        this.isLikedByMe = isLikedByMe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ParseObject parseObject = (ParseObject) o;

        if (!getObjectId().equalsIgnoreCase(parseObject.getObjectId())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return getObjectId().hashCode();
    }

}
