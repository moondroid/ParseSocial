package it.moondroid.parsesocial;

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

import com.parse.ParseException;
import com.parse.SaveCallback;

import it.moondroid.sociallib.entities.Post;
import it.moondroid.sociallib.fragments.AllPostsFragment;


public class AllPostsActivity extends ActionBarActivity {

    private int counter = 1;
    private AllPostsFragment postsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_posts);
        if (savedInstanceState == null) {
            postsFragment = new AllPostsFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, postsFragment, "AllPostsFragment")
                    .commit();
        }else {
            postsFragment = (AllPostsFragment) getSupportFragmentManager().findFragmentByTag("AllPostsFragment");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all_posts, menu);
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

        if (id == R.id.action_add_post){
            Post post = new Post("post di prova "+counter);
            counter++;
            post.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null){
                        postsFragment.update();
                    }
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }


}
