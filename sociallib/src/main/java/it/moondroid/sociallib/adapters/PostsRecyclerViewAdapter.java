package it.moondroid.sociallib.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.IconTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import it.moondroid.sociallib.R;
import it.moondroid.sociallib.entities.Post;
import it.moondroid.sociallib.widgets.LikeIconTextView;

/**
 * Created by marco.granatiero on 01/12/2014.
 */

public class PostsRecyclerViewAdapter extends RecyclerView.Adapter<PostsRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";

    private Context mContext;
    private ArrayList<Post> mDataSet = new ArrayList<>();

    private static OnRecyclerViewItemClickListener mListener;


    /**
     * Provide a reference to the type of views that you are using (custom viewholder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mDescriptionTextView;
        private TextView mDateTextView;
        private TextView mUserTextView;
        private IconTextView mCommentsTextView;
        private LikeIconTextView mLikesTextView;

        public ViewHolder(View v) {
            super(v);

            mDescriptionTextView = (TextView) v.findViewById(R.id.post_content);
            mUserTextView = (TextView) v.findViewById(R.id.user_name);
            mDateTextView = (TextView) v.findViewById(R.id.post_date);
            mCommentsTextView = (IconTextView) v.findViewById(R.id.post_num_comments);
            mLikesTextView = (LikeIconTextView) v.findViewById(R.id.post_num_likes);

            v.setOnClickListener(this);
        }

        public TextView getDescriptionTextView() {
            return mDescriptionTextView;
        }

        public TextView getDateTextView() {
            return mDateTextView;
        }

        public TextView getUserTextView() {
            return mUserTextView;
        }

        public IconTextView getCommentsIconTextView() {
            return mCommentsTextView;
        }

        public LikeIconTextView getLikesIconTextView() {
            return mLikesTextView;
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.post_container){
                mListener.onItemClick(v, getPosition());
            }
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     */
    public PostsRecyclerViewAdapter(Context context) {
        mContext = context;
        mListener = new OnRecyclerViewItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                Log.d("RecyclerViewCustomAdapter", "onPostClick "+position);
            }
        };
        loadObjects();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_post, viewGroup, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.getDescriptionTextView().setText(mDataSet.get(position).getText());
        viewHolder.getUserTextView().setText(mDataSet.get(position).getFromUser().getUsername());
        String dateString = DateUtils.getRelativeTimeSpanString(mContext, mDataSet.get(position).getDate().getTime()).toString();
        viewHolder.getDateTextView().setText(dateString);
        viewHolder.getCommentsIconTextView().setText(String.format(mContext.getResources().
                getString(R.string.comments_count), mDataSet.get(position).getNumComments()));
        viewHolder.getLikesIconTextView().setLikeCount(mDataSet.get(position).getNumLikes());
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener){
        if(listener != null){
            mListener = listener;
        }
    }

    public Post getItem(int position){
        return mDataSet.get(position);
    }

    public void loadObjects(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.include("from"); //retrieve user also
        query.orderByDescending("date"); //lastest posts first
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    // Success!
                    Toast.makeText(mContext, "found " + parseObjects.size() + " objects", Toast.LENGTH_SHORT).show();
                    mDataSet.clear();
                    for(ParseObject object : parseObjects){
                        mDataSet.add((Post) object);
                    }
                    notifyDataSetChanged();

                }else {
                    // Failure!
                    Log.e("ReadObjectsActivity.findInBackground.done", "error: " + e.getLocalizedMessage());
                    Toast.makeText(mContext, "error", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}