package it.moondroid.sociallib.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import it.moondroid.sociallib.R;
import it.moondroid.sociallib.it.moondroid.sociallib.adapters.PostsQueryAdapter;

/**
 * Created by marco.granatiero on 24/11/2014.
 */
public class AllPostsFragment extends Fragment implements AdapterView.OnItemClickListener {

    private PostsQueryAdapter adapter;

    public AllPostsFragment() {
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

    }


    public void update(){
        adapter.loadObjects();
    }


}
