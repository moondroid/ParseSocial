package it.moondroid.parsesocial;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SaveCallback;

import it.moondroid.sociallib.entities.Post;
import it.moondroid.sociallib.fragments.AllPostsFragment;
import it.moondroid.sociallib.fragments.CreatePostDialogFragment;
import it.moondroid.sociallib.fragments.CreatePostDialogInterface;
import me.drakeet.materialdialog.MaterialDialog;


public class AllPostsActivity extends ActionBarActivity implements AllPostsFragment.OnPostSelectedListener {

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
        } else {
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

        if (id == R.id.action_add_post) {

            new CreatePostDialogFragment.Builder(this).setSendPostResultListener(new CreatePostDialogInterface.OnResultListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(AllPostsActivity.this, "post pubblicato", Toast.LENGTH_SHORT).show();
                    postsFragment.update();
                }

                @Override
                public void onError(ParseException e) {
                    Toast.makeText(AllPostsActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }).show();


        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPostSelected(String postId) {
        Intent i = new Intent(this, PostDetailActivity.class);
        i.putExtra(PostDetailActivity.EXTRA_POST_ID, postId);
        startActivity(i);
    }
}
