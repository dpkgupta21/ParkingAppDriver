package com.parking.app.parkingappdriver.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class BaseActivity extends AppCompatActivity implements OnClickListener,
        CompoundButton.OnCheckedChangeListener {


    public Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(BaseFragment.this));
    }

    //	public Toolbar setToolbarTitle(String title){
//		toolbar = (Toolbar) findViewById(R.id.toolbar);
//		((TextView)toolbar.findViewById(R.id.toolbar_title)).setText(title);
//		setSupportActionBar(toolbar);
//		getSupportActionBar().setDisplayShowTitleEnabled(false);
//		return toolbar;
//	}
    public View setTouchNClick(int id) {

        View v = findViewById(id);
        v.setOnClickListener(this);
        return v;
    }

    public View setClick(int id) {

        View v = findViewById(id);
        v.setOnClickListener(this);
        return v;
    }

    public void setViewEnable(int id, boolean flag) {
        View v = findViewById(id);
        v.setEnabled(flag);
    }

    public void setViewVisibility(int id, int flag) {
        View v = findViewById(id);
        v.setVisibility(flag);
    }

    public void setTextViewText(int id, String text) {
        ((TextView) findViewById(id)).setText(text);
    }


    public void setTextViewHtmlText(int id, String text) {
        TextView tv = ((TextView) findViewById(id));
        tv.setText(Html.fromHtml(text));
    }

    public void setTextViewTextColor(int id, int color) {
        ((TextView) findViewById(id)).setTextColor(color);
    }

    public void setEditText(int id, String text) {
        ((EditText) findViewById(id)).setText(text);
    }

    public String getEditTextText(int id) {
        return ((EditText) findViewById(id)).getText().toString();
    }

    public String getTextViewText(int id) {
        return ((TextView) findViewById(id)).getText().toString();
    }

    public String getButtonText(int id) {
        return ((Button) findViewById(id)).getText().toString();
    }

    public void setButtonText(int id, String text) {
        ((Button) findViewById(id)).setText(text);
    }

    public void replaceButtoImageWith(int replaceId, int drawable) {
        ((Button) findViewById(replaceId)).setBackgroundResource(drawable);
    }


    public void chnageBackGround(int id, Drawable drawable) {
        ((Button) findViewById(id)).setBackground(drawable);

    }

    public void setButtonSelected(int id, boolean flag) {
        ((Button) findViewById(id)).setSelected(flag);
    }

    public void setTextColor(int id, int color) {
        ((Button) findViewById(id)).setTextColor(getResources().getColor(color));
    }

    public boolean isButtonSelected(int id) {
        return ((Button) findViewById(id)).isSelected();
    }

    /**
     * Method use to set view selected
     *
     * @param id   resource id of view.
     * @param flag true if view want to selected else false
     */
    public void setViewSelected(int id, boolean flag) {
        View v = findViewById(id);
        v.setSelected(flag);
    }

    /**
     * Method use to set text on TextView, EditText, Button.
     *
     * @param id   resource of TextView, EditText, Button.
     * @param text string you want to set on TextView, EditText, Button
     */
    public void setViewText(int id, String text) {
        View v = ((View) findViewById(id));
        if (v instanceof TextView) {
            TextView tv = (TextView) v;
            tv.setText(text);
        }
        if (v instanceof EditText) {
            EditText et = (EditText) v;
            et.setText(text);
        }
        if (v instanceof Button) {
            Button btn = (Button) v;
            btn.setText(text);
        }

    }

    public void setViewText(View view, int id, String text) {
        View v = ((View) view.findViewById(id));
        if (v instanceof TextView) {
            TextView tv = (TextView) v;
            tv.setText(text);
        }
        if (v instanceof EditText) {
            EditText et = (EditText) v;
            et.setText(text);
        }
        if (v instanceof Button) {
            Button btn = (Button) v;
            btn.setText(text);
        }

    }

    /**
     * Method use to get Text from TextView, EditText, Button.
     *
     * @param id resource id TextView, EditText, Button
     * @return string text from view
     */
    public String getViewText(int id) {
        String text = "";
        View v = ((View) findViewById(id));
        if (v instanceof TextView) {
            TextView tv = (TextView) v;
            text = tv.getText().toString().trim();
        }
        if (v instanceof EditText) {
            EditText et = (EditText) v;
            text = et.getText().toString().trim();
        }
        if (v instanceof Button) {
            Button btn = (Button) v;
            text = btn.getText().toString().trim();
        }
        return text;
    }

    public boolean isRadioChecked(int id) {
        RadioButton rb = (RadioButton) findViewById(id);
        return rb.isChecked();
    }

    public boolean isCheckboxChecked(int id) {
        CheckBox cb = (CheckBox) findViewById(id);
        return cb.isChecked();
    }

    public void setCheckboxChecked(int id, boolean flag) {
        CheckBox cb = (CheckBox) findViewById(id);
        cb.setChecked(flag);
    }

    public void setOnCheckbox(int id) {
        CheckBox cb = (CheckBox) findViewById(id);
        cb.setOnCheckedChangeListener(this);
    }

    public void setImageResourseBackground(int id, int resId) {
        ImageView iv = (ImageView) findViewById(id);
        iv.setImageResource(resId);
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }

    @Override
    public void onClick(View view) {

    }





}
