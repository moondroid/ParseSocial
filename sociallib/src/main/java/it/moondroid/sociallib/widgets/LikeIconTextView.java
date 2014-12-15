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

    }


    public boolean toggle(){
        isLikedByMe = !isLikedByMe;
        if(isLikedByMe){
            likeCount++;
        }else {
            likeCount--;
        }
        setIconAndCount();
        return isLikedByMe;
    }

    public int getLikeCount(){
        return likeCount;
    }

    public void setLikeCount(int count){
        likeCount = count;
        setIconAndCount();
    }

    public boolean isLikedByMe(){
        return isLikedByMe;
    }

    public void setLikedByMe(boolean likedByMe){
        isLikedByMe = likedByMe;
        setIconAndCount();
    }

    private void setIconAndCount(){
        if(isLikedByMe){
            setText("{fa-star} "+likeCount);
        }else {
            setText("{fa-star-o} "+likeCount);
        }
    }


}
