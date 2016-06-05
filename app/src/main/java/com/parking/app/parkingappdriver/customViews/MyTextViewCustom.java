package com.parking.app.parkingappdriver.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by deepak on 16/11/15.
 */

public class MyTextViewCustom extends TextView {

    public MyTextViewCustom(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextViewCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextViewCustom(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/arcon_regular.otf");
        setTypeface(tf);
    }

}


