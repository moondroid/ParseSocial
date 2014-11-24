package it.moondroid.sociallib.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import it.moondroid.sociallib.R;
import it.moondroid.sociallib.adapters.PostsQueryAdapter;

/**
 * Created by marco.granatiero on 24/11/2014.
 */
public class AllPostsFragment extends Fragment implements AdapterView.OnItemClickListener {

    private PostsQueryAdapter adapter;

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

        ListView objectListView = (ListView)fragmentView.findViewById(R.id.objects_list);
        adapter = new PostsQueryAdapter(getActivity());
        //adapter.setTextKey("text");
        //adapter.setImageKey("photo");

        objectListView.setAdapter(adapter);
        objectListView.setEmptyView(fragmentView.findViewById(R.id.empty_list));

        objectListView.setOnItemClickListener(this);

        return fragmentView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String postId = adapter.getItem(position).getObjectId();
        mListener.onPostSelected(postId);
    }


    public void update(){
        adapter.loadObjects();
    }


}
