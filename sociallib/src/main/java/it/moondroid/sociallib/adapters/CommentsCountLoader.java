package it.moondroid.sociallib.adapters;

import android.content.Context;
import android.util.Log;
import android.widget.IconTextView;

import com.parse.CountCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import it.moondroid.sociallib.R;
import it.moondroid.sociallib.entities.Post;

/**
 * Created by marco.granatiero on 26/11/2014.
 */
public class CommentsCountLoader extends ParseQuery<ParseObject> {

    private final Context context;
    private static HashMap<Post, Integer> comments = new HashMap<>();
    private WeakReference<IconTextView> iconTextViewReference;

    public CommentsCountLoader(Context context) {
        super("Comment");
        this.context = context;
    }

    public void loadCommentsCount(final Post postObject, IconTextView iconTextView, boolean forceUpdate) {
        if(forceUpdate){
            comments.remove(postObject);
        }
        loadCommentsCount(postObject, iconTextView);
    }

    public void loadCommentsCount(final Post postObject, IconTextView iconTextView) {
        if (comments.containsKey(postObject)) {
            iconTextView.setText(String.format(context.getResources().getString(R.string.comments_count),
                    comments.get(postObject)));
            Log.d("comments.containsKey", "objectId: " + postObject.getObjectId());
        } else {
            whereEqualTo("postId", postObject.getObjectId());
            iconTextView.setTag(this);
            iconTextViewReference = new WeakReference<IconTextView>(iconTextView);
            countInBackground(new CountCallback() {
                @Override
                public void done(int count, ParseException e) {
                    if (e == null) {
                        comments.put(postObject, count);
                        Log.d("comments.put", "objectId: " + postObject.getObjectId() + " size:" + comments.size());
                        if (iconTextViewReference != null) {
                            IconTextView iconTextView = iconTextViewReference.get();
                            CommentsCountLoader bitmapDownloaderTask = (CommentsCountLoader) iconTextView.getTag();
                            // Change text only if this process is still associated with it
                            if (CommentsCountLoader.this == bitmapDownloaderTask) {
                                iconTextView.setText(String.format(context.getResources().getString(R.string.comments_count),
                                        count));

                            }
                        }
                    }
                }
            });
        }

    }


}
