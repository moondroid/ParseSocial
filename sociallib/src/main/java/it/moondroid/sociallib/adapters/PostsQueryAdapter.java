package it.moondroid.sociallib.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import it.moondroid.sociallib.R;

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
                //query.whereEqualTo("highPri", true);
                return query;
            }
        });

    }


    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.item_post, null);
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

        return v;
    }
}
