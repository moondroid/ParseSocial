package it.moondroid.parsesocial;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class ReadObjectsActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private static final int REQUEST_CODE_UPDATE = 1;
    private ArrayList<ParseObject> objects = new ArrayList<ParseObject>();
    private ArrayList<String> players = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_objects);

        ParseAnalytics.trackEvent("ReadObjectsActivity");

        ListView objectListView = (ListView)findViewById(R.id.objects_list);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, players);
        objectListView.setAdapter(adapter);

        if (savedInstanceState == null){
            updateObjects();
        }


        objectListView.setOnItemClickListener(this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_read_objects, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(ReadObjectsActivity.this, SaveObjectActivity.class);
        i.putExtra("EXTRA_OBJECT_ID", objects.get(position).getObjectId());
        startActivityForResult(i, REQUEST_CODE_UPDATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_UPDATE && resultCode == RESULT_OK){
            updateObjects();
        }
    }


    private void updateObjects(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("GameScore");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    // Success!
                    Toast.makeText(ReadObjectsActivity.this, "found "+parseObjects.size()+" objects", Toast.LENGTH_SHORT).show();
                    objects.clear();
                    players.clear();
                    for(ParseObject object : parseObjects){
                        Log.d("ReadObjectsActivity.findInBackground.done", "id: "+object.getObjectId()+" playerName: "+object.getString("playerName"));
                        players.add(object.getString("playerName"));
                        objects.add(object);
                    }
                    adapter.notifyDataSetChanged();

                }else {
                    // Failure!
                    Log.e("ReadObjectsActivity.findInBackground.done", "error: " + e.getLocalizedMessage());
                    Toast.makeText(ReadObjectsActivity.this, "error", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
