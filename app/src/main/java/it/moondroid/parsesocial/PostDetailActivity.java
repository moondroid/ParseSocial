package it.moondroid.parsesocial;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import it.moondroid.sociallib.fragments.AllPostsFragment;
import it.moondroid.sociallib.fragments.PostDetailFragment;


public class PostDetailActivity extends ActionBarActivity {

    public static final String EXTRA_POST_ID = "PostDetailActivity.EXTRA_POST_ID";
    private PostDetailFragment postFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        String postId = getIntent().getStringExtra(EXTRA_POST_ID);

        if (savedInstanceState == null) {
            postFragment = PostDetailFragment.newInstance(postId);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, postFragment, "postFragment")
                    .commit();
        } else {
            postFragment = (PostDetailFragment) getSupportFragmentManager().findFragmentByTag("postFragment");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_post_detail, menu);
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
