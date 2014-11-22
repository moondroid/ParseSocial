package it.moondroid.parsesocial;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.GetCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;


public class SaveObjectActivity extends ActionBarActivity implements View.OnClickListener {

    private EditText playerNameEditText, scoreEditText;
    private CheckBox cheatsCheckBox;
    private ParseObject object;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_object);

        ParseAnalytics.trackEvent("SaveObjectActivity");

        playerNameEditText = (EditText)findViewById(R.id.playerName);
        scoreEditText = (EditText)findViewById(R.id.score);
        cheatsCheckBox = (CheckBox)findViewById(R.id.cheats);

        if (getIntent().hasExtra("EXTRA_OBJECT_ID")){
            String objectId = getIntent().getStringExtra("EXTRA_OBJECT_ID");
            ParseQuery<ParseObject> query = ParseQuery.getQuery("GameScore");
            query.getInBackground(objectId, new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    if (e == null) {
                        // Success!
                        object = parseObject;
                        playerNameEditText.setText(parseObject.getString("playerName"));
                        scoreEditText.setText(parseObject.getString("score"));
                        cheatsCheckBox.setChecked(parseObject.getBoolean("cheatMode"));
                    }else {
                        // Failure!
                        Log.e("SaveObjectActivity.getInBackground.done", "error: " + e.getLocalizedMessage());
                        Toast.makeText(SaveObjectActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        findViewById(R.id.button_send).setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send_object, menu);
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

        if (id == R.id.action_delete) {
            if (object != null){
                object.deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            // Success!
                            String objectId = object.getObjectId();
                            Log.d("SaveObjectActivity.deleteInBackground.done", "objectId: "+objectId);
                            Toast.makeText(SaveObjectActivity.this, "deleted", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            // Failure!
                            Log.e("SaveObjectActivity.deleteInBackground.done", "error: "+e.getLocalizedMessage());
                            Toast.makeText(SaveObjectActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(SaveObjectActivity.this, "no object to delete", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        String playerName = playerNameEditText.getText().toString();
        String score = scoreEditText.getText().toString();
        boolean cheats = cheatsCheckBox.isChecked();

        if (object == null){
            object = new ParseObject("GameScore");
        }

        object.put("playerName", playerName);
        object.put("score", score);
        object.put("cheatMode", cheats);
        object.saveInBackground(new SaveCallback () {

            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // Success!
                    String objectId = object.getObjectId();
                    Log.d("SaveObjectActivity.saveInBackground.done", "objectId: "+objectId);
                    Toast.makeText(SaveObjectActivity.this, "saved", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    // Failure!
                    Log.e("SaveObjectActivity.saveInBackground.done", "error: "+e.getLocalizedMessage());
                    Toast.makeText(SaveObjectActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
