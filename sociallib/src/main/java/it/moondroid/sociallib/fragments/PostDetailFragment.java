package it.moondroid.sociallib.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.IconTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import it.moondroid.sociallib.R;
import it.moondroid.sociallib.adapters.CommentsCountLoader;
import it.moondroid.sociallib.adapters.CommentsQueryAdapter;
import it.moondroid.sociallib.entities.Comment;
import it.moondroid.sociallib.entities.Post;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by marco.granatiero on 24/11/2014.
 */
public class PostDetailFragment extends Fragment implements AdapterView.OnItemClickListener {

    private static final String KEY_POST_ID = "PostDetailFragment.KEY_POST_ID";

    private Post post;
    private ListView objectListView;
    private CommentsQueryAdapter adapter;
    private TextView descriptionView, dateView, userView;
    private EditText editTextComment;
    private IconTextView numComments;

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
        editTextComment = (EditText) fragmentView.findViewById(R.id.edit_post_content);

        objectListView = (ListView)fragmentView.findViewById(R.id.comments_list);

        numComments = (IconTextView) fragmentView.findViewById(R.id.post_num_comments);


        fragmentView.findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentString = editTextComment.getText().toString();
                if(!commentString.isEmpty() && post != null){
                    final Comment comment = new Comment(post.getObjectId(), commentString);
                    //post.addComment(comment);

                    final MaterialDialog pd = new MaterialDialog(getActivity())
                            .setContentView(getActivity().getLayoutInflater().inflate(R.layout.dialog_indeterminate, null))
                            .setCanceledOnTouchOutside(false);
                    pd.show();
                    comment.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            pd.dismiss();
                            if (e == null) {
                                // Success!
                                String objectId = comment.getObjectId();
                                Log.d("PostDetailFragment.saveInBackground.done", "objectId: "+objectId);
                                Toast.makeText(getActivity(), "comment added", Toast.LENGTH_SHORT).show();
                                editTextComment.setText("");
                                adapter.loadObjects();
                                CommentsCountLoader task = new CommentsCountLoader(getActivity());
                                task.loadCommentsCount(post, numComments, true);
                            } else {
                                // Failure!
                                Log.e("PostDetailFragment.saveInBackground.done", "error: "+e.getLocalizedMessage());
                                Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

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
                    post = (Post) parseObject;
                    descriptionView.setText(post.getString("text"));
                    android.text.format.DateFormat df = new android.text.format.DateFormat();
                    dateView.setText(df.format("dd MMMM - hh:mm", post.getDate("date")));
                    userView.setText(post.getParseUser("from").getUsername());
                    CommentsCountLoader task = new CommentsCountLoader(getActivity());
                    task.loadCommentsCount(post, numComments);

                    adapter = new CommentsQueryAdapter(getActivity(), post.getObjectId());
                    objectListView.setAdapter(adapter);
                }else {
                    // Failure!
                    Log.e("PostDetailFragment.getInBackground.done", "error: " + e.getLocalizedMessage());
                    Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
