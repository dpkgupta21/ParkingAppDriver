package com.parking.app.parkingappdriver.customViews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


public class MyTextView25 extends TextView {

    public MyTextView25(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextView25(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextView25(Context context) {
        super(context);
        init();
    }

    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/arcon_regular.otf");
        setTypeface(tf);
        setTextSize(25);
    }

}


