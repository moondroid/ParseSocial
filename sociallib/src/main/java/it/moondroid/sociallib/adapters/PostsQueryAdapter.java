package it.moondroid.sociallib.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.IconTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import it.moondroid.sociallib.R;
import it.moondroid.sociallib.entities.Post;
import it.moondroid.sociallib.widgets.LikeIconTextView;

/**
 * Created by marco.granatiero on 24/11/2014.
 */
public class PostsQueryAdapter extends ParseQueryAdapter {

    public PostsQueryAdapter(Context context) {
        //super(context, "Post");
        super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("Post");
                query.include("from"); //retrieve user also
                query.orderByDescending("date"); //lastest posts first
                //query.whereEqualTo("highPri", true);
                return query;
            }
        });
    }


    @Override
    public View getItemView(final ParseObject object, View v, ViewGroup parent) {

        final ViewHolderItem viewHolder;

        if (v == null) {
            v = View.inflate(getContext(), R.layout.item_post, null);

            viewHolder = new ViewHolderItem();
            viewHolder.commentsTextView = (IconTextView) v.findViewById(R.id.post_num_comments);
            viewHolder.likesTextView = (LikeIconTextView) v.findViewById(R.id.post_num_likes);
            v.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolderItem) v.getTag();
        }

        // Take advantage of ParseQueryAdapter's getItemView logic for
        // populating the main TextView/ImageView.
        // The IDs in your custom layout must match what ParseQueryAdapter expects
        // if it will be populating a TextView or ImageView for you.
        //super.getItemView(object, v, parent);

        // Do additional configuration before returning the View.
        TextView descriptionView = (TextView) v.findViewById(R.id.post_content);
        descriptionView.setText(object.getString("text"));

        TextView dateView = (TextView) v.findViewById(R.id.post_date);
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        dateView.setText(df.format("dd MMMM - hh:mm", object.getDate("date")));

        TextView userView = (TextView) v.findViewById(R.id.user_name);
        userView.setText(object.getParseUser("from").getUsername());


        CommentsCountLoader task = new CommentsCountLoader(getContext());
        task.loadCommentsCount((Post) object, viewHolder.commentsTextView);

        return v;
    }

    // our ViewHolder.
    // caches our TextView
    static class ViewHolderItem {
        IconTextView commentsTextView;
        LikeIconTextView likesTextView;
    }

}
