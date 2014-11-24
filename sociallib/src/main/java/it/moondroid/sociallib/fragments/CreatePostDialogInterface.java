package it.moondroid.sociallib.fragments;

import android.view.KeyEvent;

/**
 * Created by marco.granatiero on 24/11/2014.
 */
public interface CreatePostDialogInterface {

    public static final int BUTTON_SEND_POST = -1;

    public void dismiss();

    public String getContentText();

    interface OnClickListener {
        public void onClick(CreatePostDialogInterface dialog, int which);
    }

    interface OnKeyListener {
        public boolean onKey(CreatePostDialogInterface dialog, int keyCode, KeyEvent event);
    }
}
