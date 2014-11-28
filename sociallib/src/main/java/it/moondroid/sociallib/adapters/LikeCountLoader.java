package it.moondroid.sociallib.adapters;

import android.content.Context;
import android.widget.IconTextView;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.WeakHashMap;

import it.moondroid.sociallib.R;
import it.moondroid.sociallib.entities.Like;
import it.moondroid.sociallib.entities.Post;

/**
 * Created by marco.granatiero on 26/11/2014.
 */
public class LikeCountLoader {

    private final Context context;

    //Create Map (collection) to store image and image url in key value pair
    private Map<IconTextView, String> iconTextViews = Collections.synchronizedMap(
            new WeakHashMap<IconTextView, String>());

    private Map<String, Integer> memoryCache = Collections.synchronizedMap(
            new LinkedHashMap<String, Integer>());

    public LikeCountLoader(Context context) {
        this.context = context;
    }

    public void loadLikeCount(Post post, IconTextView iconTextView, boolean forceUpdate) {
        if(forceUpdate){
            memoryCache.remove(post.getObjectId());
        }
        loadLikeCount(post, iconTextView);
    }

    public void setLikeCount (Post post, int count){
        memoryCache.put(post.getObjectId(), count);
    }

    public void loadLikeCount(Post post, IconTextView iconTextView) {
        //Store image and url in Map
        iconTextViews.put(iconTextView, post.getObjectId());

        //Check image is stored in MemoryCache Map or not (see MemoryCache.java)
        Integer count = memoryCache.get(post.getObjectId());

        if(count!=null){
            // if image is stored in MemoryCache Map then
            // Show image in listview row
            updateText(iconTextView, count, false);//TODO

        } else {
            // Store image and url in PhotoToLoad object
            //TODO
            final CountToLoad countToLoad = new CountToLoad(post.getObjectId(), iconTextView);
//
            //queue Photo to download from url
            ParseQuery query = new ParseQuery<ParseObject>("Like");
            query.whereEqualTo("post", post);
            query.countInBackground(new CountCallback() {
                @Override
                public void done(int count, ParseException e) {
                    if (e == null) {
                        // set image data in Memory Cache
                        memoryCache.put(countToLoad.postId, count);

                        if(iconTextViewReused(countToLoad))
                            return;

                        updateText(countToLoad.iconTextView, count, false);//TODO
                    }
                }
            });

            //Before downloading image show default image
            //imageView.setImageResource(stub_id);
        }

    }

    private void updateText(IconTextView iconTextView, int count, boolean likedByMe){
        if(likedByMe){
            iconTextView.setText(String.format(context.getResources().getString(R.string.likes_count_liked),
                    count));
        }else {
            iconTextView.setText(String.format(context.getResources().getString(R.string.likes_count),
                    count));
        }
    }

    private boolean iconTextViewReused(CountToLoad countToLoad){

        String tag = iconTextViews.get(countToLoad.iconTextView);
        //Check url is already exist in imageViews MAP
        if(tag==null || !tag.equals(countToLoad.postId))
            return true;
        return false;
    }

    public void modifyLike (Post post, boolean like){
        if (like){
            Integer count = memoryCache.get(post.getObjectId());
            if(count!=null){
                memoryCache.put(post.getObjectId(), count+1);
            }else {
                memoryCache.put(post.getObjectId(), 1);
            }
            addNewLike(post);
        }else {
            //TODO
        }
    }

    private void addNewLike(final Post post){

        Like like = new Like(post);
        like.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    // Failure!
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                    memoryCache.remove(post.getObjectId());
                }else {
                    Toast.makeText(context, "liked", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    private void removeLike(Post post){
//        if (like != null){
//            like.deleteInBackground(new DeleteCallback() {
//                @Override
//                public void done(ParseException e) {
//                    if (e != null) {
//                        // Failure!
//                        Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
//                        toggle();
//                    }else {
//                        Toast.makeText(context, "disliked", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        } else {
//            Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
//        }
//    }

    private class CountToLoad
    {
        public String postId;
        public IconTextView iconTextView;
        public CountToLoad(String u, IconTextView i){
            postId=u;
            iconTextView=i;
        }
    }
}
