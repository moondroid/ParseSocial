package it.moondroid.sociallib.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.IconTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FunctionCallback;
import com.parse.GetCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.HashMap;

import it.moondroid.sociallib.R;
import it.moondroid.sociallib.adapters.CommentsQueryAdapter;
import it.moondroid.sociallib.entities.Comment;
import it.moondroid.sociallib.entities.Like;
import it.moondroid.sociallib.entities.Post;
import it.moondroid.sociallib.widgets.LikeIconTextView;
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
    private LikeIconTextView numLikes;

    private OnPostDetailListener mListener;

    public interface OnPostDetailListener {
        public void onPostDeleted();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnPostDetailListener) activity;
        } catch (ClassCastException e) {
            mListener = new OnPostDetailListener() {
                @Override
                public void onPostDeleted() {
                    //do nothing
                }
            };
        }
    }

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
        setHasOptionsMenu(true);
        updatePost();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_post_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_delete) {
            final MaterialDialog confirmDialog = new MaterialDialog(getActivity())
                    .setTitle("Elimina")
                    .setMessage("Eliminare definitivamente questo post?");
            confirmDialog.setNegativeButton("Annulla", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmDialog.dismiss();
                }
            });
            confirmDialog.setPositiveButton("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmDialog.dismiss();
                    final MaterialDialog progressDialog = new MaterialDialog(getActivity())
                            .setContentView((getActivity().getLayoutInflater().inflate(R.layout.dialog_indeterminate, null)));
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    post.deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            progressDialog.dismiss();
                            if(e == null){
                                mListener.onPostDeleted();
                            }else {
                                Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            });
            confirmDialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        numLikes = (LikeIconTextView) fragmentView.findViewById(R.id.post_num_likes);
        numLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LikeIconTextView likeIconTextView = (LikeIconTextView)v;
                final boolean liked = likeIconTextView.toggle();

                Like.toggleLike(post, liked, new Like.LikeCallBack() {
                    @Override
                    public void onSuccess(boolean liked) {
                        String message = liked? "liked" : "unliked";
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ParseException e) {
                        Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


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

                                updatePost();

                                //numLikes.setPost(post);

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


    public void updatePost(){
        //adapter.loadObjects();

        final String postId = getArguments().getString(KEY_POST_ID);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.include("from"); //retrieve user also
        query.include("user_array_likes"); //retrieve user likes also
        query.getInBackground(postId, new GetCallback<ParseObject>() {

            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    // Success!
                    post = (Post) parseObject;
                    descriptionView.setText(post.getText());
                    String dateString = DateUtils.getRelativeTimeSpanString(getActivity(), post.getDate().getTime()).toString();
                    dateView.setText(dateString);
                    userView.setText(post.getFromUser().getUsername());

                    numComments.setText(String.format(getResources().
                            getString(R.string.comments_count), post.getNumComments()));

                    numLikes.setLikeCount(post.getNumLikes());
                    numLikes.setLikedByMe(post.isLikedByMe());

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

//    public void updatePost(){
//        //adapter.loadObjects();
//
//        final String postId = getArguments().getString(KEY_POST_ID);
//
//        HashMap<String, Object> params = new HashMap<String, Object>();
//        params.put("postId", postId);
//        //params.put("user", ParseUser.getCurrentUser());
//        ParseCloud.callFunctionInBackground("getPostWithLike", params, new FunctionCallback<Object>() {
//
//            @Override
//            public void done(Object result, ParseException e) {
//                if (e == null) {
//                    // Success!
//                    HashMap<String , Object> receivedResult = (HashMap<String, Object>)result;
//                    post = (Post) receivedResult.get("post");
//                    boolean isLikedByMe = (boolean) receivedResult.get("isLikedByMe");
//                    descriptionView.setText(post.getString("text"));
//                    android.text.format.DateFormat df = new android.text.format.DateFormat();
//                    dateView.setText(df.format("dd MMMM - hh:mm", post.getDate("date")));
//                    userView.setText(post.getParseUser("from").getUsername());
//
//                    numComments.setText(String.format(getResources().
//                            getString(R.string.comments_count), post.getNumber("comments").intValue()));
//
//                    numLikes.setLikeCount(post.getNumber("num_likes").intValue());
//                    numLikes.setLikedByMe(isLikedByMe);
//
//                    adapter = new CommentsQueryAdapter(getActivity(), post.getObjectId());
//                    objectListView.setAdapter(adapter);
//                }else {
//                    // Failure!
//                    Log.e("PostDetailFragment.getInBackground.done", "error: " + e.getLocalizedMessage());
//                    Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//
//    }

}
