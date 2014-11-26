package it.moondroid.sociallib.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.IconTextView;
import android.widget.Toast;

/**
 * Created by marco.granatiero on 26/11/2014.
 */
public class LikeIconTextView extends IconTextView {

    private boolean isLikedByMe = false;
    private int likeCount = 0;

    public LikeIconTextView(Context context) {
        super(context);
        init();
    }

    public LikeIconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LikeIconTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){

        setText("{fa-star-o} "+likeCount);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                isLikedByMe = !isLikedByMe;
                if(isLikedByMe){
                    likeCount++;
                    setText("{fa-star} "+likeCount);
                }else {
                    likeCount--;
                    setText("{fa-star-o} "+likeCount);
                }

                Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
