package it.moondroid.sociallib.entities;

import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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

    private static LikeCallBack emptyCallBack = new LikeCallBack() {
        @Override
        public void onSuccess(boolean liked) {
            //do nothing
        }

        @Override
        public void onError(ParseException e) {
           //do nothing
        }
    };

    public interface LikeCallBack {
        public void onSuccess(boolean liked);
        public void onError(ParseException e);
    }

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

    public static void toggleLike(final Post post, boolean liked){
        toggleLike(post, liked, Like.emptyCallBack);
    }

    public static void toggleLike(final Post post, boolean liked, final LikeCallBack callBack){
        if(liked){
            addNewLike(post, callBack);
        }else {
            removeLike(post, callBack);
        }
    }

    public static void addNewLike(final Post post){
        addNewLike(post, Like.emptyCallBack);
    }

    public static void addNewLike(final Post post, final LikeCallBack callBack){

        Like like = new Like(post);
        like.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    callBack.onSuccess(true);
                }else {
                    // Failure!
                    callBack.onError(e);
                }
            }
        });
    }

    public static void removeLike(final Post post){
        removeLike(post, Like.emptyCallBack);
    }

    public static void removeLike(final Post post, final LikeCallBack callBack){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Like");
        query.whereEqualTo("post", post);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null && parseObjects.size() > 0){
                    parseObjects.get(0).deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null){
                                callBack.onSuccess(false);
                            }else{
                                // Failure!
                                callBack.onError(e);
                            }
                        }
                    });
                }else {
                    // Failure!
                    callBack.onError(e);
                }
            }
        });

    }

}
