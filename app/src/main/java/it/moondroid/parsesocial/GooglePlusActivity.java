package it.moondroid.parsesocial;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import it.moondroid.sociallib.fragments.GooglePlusFragment;


public class GooglePlusActivity extends ActionBarActivity {

    GooglePlusFragment googlePlusFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_plus);
        if (savedInstanceState == null) {
            googlePlusFragment = new GooglePlusFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, googlePlusFragment, "GooglePlusFragment")
                    .commit();
        }else {
            googlePlusFragment = (GooglePlusFragment) getSupportFragmentManager()
                    .findFragmentByTag("GooglePlusFragment");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//        if (requestCode == GooglePlusFragment.REQUEST_CODE_SIGN_IN
//                || requestCode == GooglePlusFragment.REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES){
//            GooglePlusFragment fragment = (GooglePlusFragment) getSupportFragmentManager()
//                    .findFragmentByTag("GooglePlusFragment");
//            fragment.onActivityResult(requestCode, resultCode, data);
//        } else {
//            super.onActivityResult(requestCode, resultCode, data);
//        }
        googlePlusFragment.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
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

}
