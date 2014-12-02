package it.moondroid.sociallib.adapters;

import android.view.View;

/**
 * Created by marco.granatiero on 02/12/2014.
 */
public interface OnRecyclerViewPostClickListener extends OnRecyclerViewItemClickListener {
    public void onLikeClick(View view, int position);
}
