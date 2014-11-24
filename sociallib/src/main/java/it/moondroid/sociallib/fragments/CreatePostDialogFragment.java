package it.moondroid.sociallib.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseUser;

import it.moondroid.sociallib.R;

/**
 * Created by marco.granatiero on 24/11/2014.
 */
public class CreatePostDialogFragment extends DialogFragment implements CreatePostDialogInterface {

    private Dialog dialog;
    private EditText postContentText;
    private View mView;

    private CreatePostDialogInterface.OnClickListener mSendPostButtonListener;


    public CreatePostDialogFragment() {
        super();
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        View view = setupUI(dialog);

        dialog.setContentView(view);

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {

            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }


    public void setContentView(View view) {
        dialog.setContentView(view);
    }

    @Override
    public String getContentText(){
        return postContentText.getText().toString();
    }

    public void show() {
        dialog.show();
    }


    private View setupUI(final Dialog dialog) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_new_post, null);

        ((TextView) view.findViewById(R.id.user_name)).setText(ParseUser.getCurrentUser().getUsername());

        postContentText = (EditText)view.findViewById(R.id.edit_post_content);

        view.findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSendPostButtonListener != null) {
                    mSendPostButtonListener.onClick(CreatePostDialogFragment.this, CreatePostDialogInterface.BUTTON_SEND_POST);
                }
                dialog.dismiss();
            }
        });


        return view;
    }


    public static class Builder {

        private Context context;
        private String title;
        private String message;
        private View view;
        private boolean cancelable = true;
        private CreatePostDialogInterface.OnClickListener sendPostButtonListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(int titleId) {
            this.title = context.getResources().getString(titleId);
            return this;
        }

        public Builder setTitle(CharSequence title) {
            this.title = title.toString();
            return this;
        }

        public Builder setMessage(int messageId) {
            this.message = context.getResources().getString(messageId);
            return this;
        }

        public Builder setMessage(CharSequence message) {
            this.message = message.toString();
            return this;
        }

        public Builder setView(View view) {
            this.view = view;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setSendPostButton(final CreatePostDialogInterface.OnClickListener listener) {
            this.sendPostButtonListener = listener;
            return this;
        }

        public CreatePostDialogFragment create() {
            final CreatePostDialogFragment dialog = new CreatePostDialogFragment();
            dialog.mSendPostButtonListener = sendPostButtonListener;
            dialog.mView = view;
            dialog.setCancelable(cancelable);

            return dialog;
        }

        public CreatePostDialogFragment show() {
            CreatePostDialogFragment dialog = create();
            dialog.show(((Activity) context).getFragmentManager(), "NewPostFragment");

            return dialog;
        }
    }
}
