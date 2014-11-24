package it.moondroid.sociallib.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import it.moondroid.sociallib.R;
import it.moondroid.sociallib.adapters.PostsQueryAdapter;

/**
 * Created by marco.granatiero on 24/11/2014.
 */
public class PostDetailFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String KEY_POST_ID = "PostDetailFragment.KEY_POST_ID";

    private PostsQueryAdapter adapter;
    private TextView descriptionView, dateView, userView;

    public PostDetailFragment() {
    }

    public static PostDetailFragment newInstance(String postId){
        PostDetailFragment f = new PostDetailFragment();
        Bundle args = new Bundle();
        args.putString(KEY_POST_ID, postId);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        update();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_post_detail, container, false);

        descriptionView = (TextView) fragmentView.findViewById(R.id.post_content);


        dateView = (TextView) fragmentView.findViewById(R.id.post_date);


        userView = (TextView) fragmentView.findViewById(R.id.user_name);


        ListView objectListView = (ListView)fragmentView.findViewById(R.id.comments_list);
        //adapter = new PostsQueryAdapter(getActivity());
        //adapter.setTextKey("text");
        //adapter.setImageKey("photo");

//        objectListView.setAdapter(adapter);
//        objectListView.setEmptyView(fragmentView.findViewById(R.id.empty_list));
//        objectListView.setOnItemClickListener(this);

        return fragmentView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }


    public void update(){
        //adapter.loadObjects();

        String postId = getArguments().getString(KEY_POST_ID);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.include("from"); //retrieve user also
        query.getInBackground(postId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    // Success!
                    descriptionView.setText(parseObject.getString("text"));
                    android.text.format.DateFormat df = new android.text.format.DateFormat();
                    dateView.setText(df.format("dd MMMM - hh:mm", parseObject.getDate("date")));
                    userView.setText(parseObject.getParseUser("from").getUsername());

                }else {
                    // Failure!
                    Log.e("PostDetailFragment.getInBackground.done", "error: " + e.getLocalizedMessage());
                    Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
