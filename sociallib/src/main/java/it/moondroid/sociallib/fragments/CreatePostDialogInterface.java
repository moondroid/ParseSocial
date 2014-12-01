package it.moondroid.sociallib.fragments;

import android.view.KeyEvent;

import com.parse.ParseException;

/**
 * Created by marco.granatiero on 24/11/2014.
 */
public interface CreatePostDialogInterface {

    public static final int BUTTON_SEND_POST = -1;

    interface OnResultListener {
        public void onSuccess();
        public void onError(ParseException e);
    }

    interface OnClickListener {
        public void onClick(CreatePostDialogInterface dialog, int which);
    }

    interface OnKeyListener {
        public boolean onKey(CreatePostDialogInterface dialog, int keyCode, KeyEvent event);
    }
}
