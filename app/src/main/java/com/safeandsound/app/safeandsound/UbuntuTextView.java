package com.safeandsound.app.safeandsound;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by louisapabst on 20.04.17.
 */

public class UbuntuTextView extends TextView {

    public UbuntuTextView(Context context, AttributeSet attrs){
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Ubuntu-Regular.ttf"));
    }
}
