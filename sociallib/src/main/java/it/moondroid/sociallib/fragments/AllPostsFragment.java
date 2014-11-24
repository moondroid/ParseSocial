package it.moondroid.sociallib.fragments;

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
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import it.moondroid.sociallib.R;

/**
 * Created by marco.granatiero on 24/11/2014.
 */
public class AllPostsFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ArrayList<ParseObject> objects = new ArrayList<ParseObject>();
    private ArrayList<String> titles = new ArrayList<String>();
    private ArrayAdapter<String> adapter;


    public AllPostsFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null){
            update();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_all_posts, container, false);

        if(savedInstanceState!=null){
            titles = (ArrayList<String>) savedInstanceState.getSerializable("titles");
        }

        ListView objectListView = (ListView)fragmentView.findViewById(R.id.objects_list);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, titles);
        objectListView.setAdapter(adapter);
        objectListView.setEmptyView(fragmentView.findViewById(R.id.empty_list));

        objectListView.setOnItemClickListener(this);

        return fragmentView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("titles", titles);
        super.onSaveInstanceState(outState);
    }

    public void update(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    // Success!
                    Toast.makeText(getActivity(), "found " + parseObjects.size() + " objects", Toast.LENGTH_SHORT).show();
                    objects.clear();
                    titles.clear();
                    for(ParseObject object : parseObjects){
                        Log.d("AllPostsFragment.findInBackground.done", "id: " + object.getObjectId() + " text: " + object.getString("text"));
                        titles.add(object.getString("text"));
                        objects.add(object);
                    }
                    adapter.notifyDataSetChanged();

                }else {
                    // Failure!
                    Log.e("AllPostsFragment.findInBackground.done", "error: " + e.getLocalizedMessage());
                    Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
