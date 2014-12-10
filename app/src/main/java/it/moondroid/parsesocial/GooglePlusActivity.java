package it.moondroid.parsesocial;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.plus.model.people.Person;

import it.moondroid.sociallib.fragments.GooglePlusLoginFragment;


public class GooglePlusActivity extends ActionBarActivity implements GooglePlusLoginFragment.GooglePlusLoginCallback {

    GooglePlusLoginFragment googlePlusFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_plus);
        if (savedInstanceState == null) {
            googlePlusFragment = new GooglePlusLoginFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, googlePlusFragment, "GooglePlusFragment")
                    .commit();
        }else {
            googlePlusFragment = (GooglePlusLoginFragment) getSupportFragmentManager()
                    .findFragmentByTag("GooglePlusFragment");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        googlePlusFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_google_plus, menu);
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
    public void onGooglePlusConnected(Person person) {

    }

    @Override
    public void onGooglePlusDisconnected() {

    }
}
