package com.parking.app.parkingappdriver.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by deepak on 16/11/15.
 */

public class MyTextView30 extends TextView {

    public MyTextView30(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextView30(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextView30(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/arcon_regular.otf");
        setTypeface(tf);
        setTextSize(30);
    }

}


