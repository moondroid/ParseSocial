package it.moondroid.sociallib.adapters;

import android.content.Context;
import android.util.Log;
import android.widget.IconTextView;
import android.widget.TextView;

import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.WeakHashMap;

import it.moondroid.sociallib.R;
import it.moondroid.sociallib.entities.Post;

/**
 * Created by marco.granatiero on 26/11/2014.
 */
public class CommentsCountLoader {

    private final Context context;

    //Create Map (collection) to store image and image url in key value pair
    private Map<IconTextView, String> iconTextViews = Collections.synchronizedMap(
            new WeakHashMap<IconTextView, String>());

    private Map<String, Integer> memoryCache = Collections.synchronizedMap(
            new LinkedHashMap<String, Integer>());

    public CommentsCountLoader(Context context) {
        this.context = context;
    }

    public void loadCommentsCount(String objectId, IconTextView iconTextView, boolean forceUpdate) {
        if(forceUpdate){
            memoryCache.remove(objectId);
        }
        loadCommentsCount(objectId, iconTextView);
    }

    public void loadCommentsCount(String objectId, IconTextView iconTextView) {
        //Store image and url in Map
        iconTextViews.put(iconTextView, objectId);

        //Check image is stored in MemoryCache Map or not (see MemoryCache.java)
        Integer count = memoryCache.get(objectId);

        if(count!=null){
            // if image is stored in MemoryCache Map then
            // Show image in listview row
            iconTextView.setText(String.format(context.getResources().getString(R.string.comments_count),
                    count));
        } else {
            // Store image and url in PhotoToLoad object
            final CountToLoad countToLoad = new CountToLoad(objectId, iconTextView);

            //queue Photo to download from url
            ParseQuery query = new ParseQuery<ParseObject>("Comment");
            query.whereEqualTo("postId", objectId);
            query.countInBackground(new CountCallback() {
                @Override
                public void done(int count, ParseException e) {
                    if (e == null) {
                        // set image data in Memory Cache
                        memoryCache.put(countToLoad.postId, count);

                        if(iconTextViewReused(countToLoad))
                            return;

                        countToLoad.iconTextView.setText(String.format(context.getResources().
                                getString(R.string.comments_count), count));
                    }
                }
            });

            //Before downloading image show default image
            //imageView.setImageResource(stub_id);
        }

    }

    private boolean iconTextViewReused(CountToLoad countToLoad){

        String tag = iconTextViews.get(countToLoad.iconTextView);
        //Check url is already exist in imageViews MAP
        if(tag==null || !tag.equals(countToLoad.postId))
            return true;
        return false;
    }

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
