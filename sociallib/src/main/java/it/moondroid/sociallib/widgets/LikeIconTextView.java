package it.moondroid.sociallib.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.IconTextView;

import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import it.moondroid.sociallib.entities.Like;
import it.moondroid.sociallib.entities.Post;

/**
 * Created by marco.granatiero on 26/11/2014.
 */
public class LikeIconTextView extends IconTextView {

    private Post post = null;
    private String postId = null;
    private Like like = null;
    private boolean isLikedByMe = false;
    private int likeCount = 0;

    public LikeIconTextView(Context context) {
        super(context);
        init();
    }

    public LikeIconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LikeIconTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){

        setIconAndCount();
        if(post != null){
            loadLikeCount();
            loadIsLikedByMe();
        }

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(post != null){
                    toggle();
                    if(isLikedByMe){
                        //addNewLike(); //TODO
                    }else {
                        //removeLike(); //TODO
                    }
                }

                //Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setPost(Post post){
        this.post = post;
        if(post != null){
            loadLikeCount();
            loadIsLikedByMe();
        }
    }

    public void toggle(){
        isLikedByMe = !isLikedByMe;
        if(isLikedByMe){
            likeCount++;
        }else {
            likeCount--;
        }
        setIconAndCount();
    }

    public int getLikeCount(){
        return likeCount;
    }

    private void setIconAndCount(){
        if(isLikedByMe){
            setText("{fa-star} "+likeCount);
        }else {
            setText("{fa-star-o} "+likeCount);
        }
    }

    private void loadLikeCount(){

        ParseQuery query = new ParseQuery<ParseObject>("Like");
        query.whereEqualTo("post", post);
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int count, ParseException e) {
                if(e == null){
                    likeCount = count;
                    setIconAndCount();
                }
            }
        });
    }

    private void loadIsLikedByMe(){
        ParseQuery query = new ParseQuery<ParseObject>("Like");
        query.whereEqualTo("from", ParseUser.getCurrentUser());
        query.whereEqualTo("post", post);
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int count, ParseException e) {
                if(e == null){
                    isLikedByMe = count==0? false : true;
                    setIconAndCount();
                }
            }
        });
    }

//    private void addNewLike(){
//
//        like = new Like(post);
//        like.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if(e != null){
//                    // Failure!
//                    Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
//                    toggle();
//                }else {
//                    Toast.makeText(getContext(), "liked", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//    private void removeLike(){
//        if (like != null){
//            like.deleteInBackground(new DeleteCallback() {
//                @Override
//                public void done(ParseException e) {
//                    if (e != null) {
//                        // Failure!
//                        Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
//                        toggle();
//                    }else {
//                        Toast.makeText(getContext(), "disliked", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        } else {
//            Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
//        }
//    }
}
