package it.moondroid.parsesocial;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;


public class MainActivity extends ActionBarActivity {

    private static final int REQUEST_CODE_LOGIN = 1;
    private static final int REQUEST_CODE_FACEBOOK_LOGIN = 2;

    private Button btnLogin;
    private TextView textUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null){
            ParseAnalytics.trackAppOpened(getIntent());
        }

        btnLogin = (Button)findViewById(R.id.button_login);
        textUserName = (TextView)findViewById(R.id.textUser);
        updateLoginState();

        findViewById(R.id.button_save_object).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SaveObjectActivity.class));
            }
        });

        findViewById(R.id.button_read_objects).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ReadObjectsActivity.class));
            }
        });

        findViewById(R.id.button_read_posts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AllPostsActivity.class));
            }
        });

        findViewById(R.id.button_gplus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GooglePlusActivity.class));
            }
        });

        findViewById(R.id.button_facebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseFacebookUtils.logIn(MainActivity.this, REQUEST_CODE_FACEBOOK_LOGIN, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException err) {
                        if (user == null) {
                            Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                        } else if (user.isNew()) {
                            Log.d("MyApp", "User signed up and logged in through Facebook!");
                            Log.d("MyApp", "user: "+user.getUsername()+" "+user.getEmail());
                        } else {
                            Log.d("MyApp", "User logged in through Facebook!");
                            Log.d("MyApp", "user: "+user.getUsername()+" "+user.getEmail());

                            Request.newMeRequest(ParseFacebookUtils.getSession(),
                                    new Request.GraphUserCallback() {
                                        @Override
                                        public void onCompleted(GraphUser fbUser, Response response) {
                                            Log.d("MyApp", "fbUser: "+fbUser.getUsername()+" "+fbUser.getLink());
                                            Toast.makeText(MainActivity.this, "Hello "+fbUser.getUsername(), Toast.LENGTH_SHORT).show();
                                        }
                                    }).executeAsync();
                        }
                    }
                });
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE_LOGIN && resultCode == RESULT_OK){
            updateLoginState();
        }

        if(requestCode == REQUEST_CODE_FACEBOOK_LOGIN && resultCode == RESULT_OK){
            ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
            updateLoginState();
        }
    }

    private void updateLoginState(){
        //check if the user is cached on disk
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // do stuff with the user
            textUserName.setText(currentUser.getUsername());
            textUserName.setVisibility(View.VISIBLE);
            btnLogin.setText("LOGOUT");
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ParseUser.logOut();
                    updateLoginState();
                }
            });

        } else {
            // show the signup or login screen
            textUserName.setText("");
            textUserName.setVisibility(View.GONE);
            btnLogin.setText("LOGIN");
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), REQUEST_CODE_LOGIN);
                }
            });
        }
    }
}
