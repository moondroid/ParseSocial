package it.moondroid.sociallib.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.ParseException;

import it.moondroid.sociallib.R;
import it.moondroid.sociallib.adapters.OnRecyclerViewItemClickListener;
import it.moondroid.sociallib.adapters.OnRecyclerViewPostClickListener;
import it.moondroid.sociallib.adapters.PostsRecyclerViewAdapter;
import it.moondroid.sociallib.entities.Like;
import it.moondroid.sociallib.entities.Post;
import it.moondroid.sociallib.widgets.LikeIconTextView;

/**
 * Created by marco.granatiero on 24/11/2014.
 */
public class AllPostsFragment extends Fragment implements OnRecyclerViewPostClickListener {

    private PostsRecyclerViewAdapter mAdapter;
    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;

    private OnPostSelectedListener mListener;


    public interface OnPostSelectedListener {
        public void onPostSelected(String postId);
    }


    public AllPostsFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnPostSelectedListener) activity;
        } catch (ClassCastException e) {
            mListener = new OnPostSelectedListener() {
                @Override
                public void onPostSelected(String postId) {
                    //do nothing
                }
            };
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_all_posts, container, false);

        mRecyclerView = (RecyclerView)fragmentView.findViewById(R.id.post_list);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new PostsRecyclerViewAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

//        mRecyclerView.setEmptyView(fragmentView.findViewById(R.id.empty_list));
        mAdapter.setOnItemClickListener(this);

        return fragmentView;
    }


    @Override
    public void onItemClick(View view, int position) {
        String postId = mAdapter.getItem(position).getObjectId();
        mListener.onPostSelected(postId);
        Log.d("AllPostsFragment", "onPostClick " + position);
    }

    @Override
    public void onLikeClick(View view, int position) {
        Log.d("AllPostsFragment", "onLikeClick " + position);
        Post post = mAdapter.getItem(position);
        final LikeIconTextView likeIconTextView = (LikeIconTextView)view;
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

    public void update(){
        mAdapter.loadObjects();
    }


}
