package com.safeandsound.app.safeandsound;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static java.security.AccessController.getContext;


/**
 * Created by louisapabst on 16.04.17.
 */

public class Window extends FragmentActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.window);

    }

    public void addWindow(View view){
        int dip70 = 70;
        int dip2 = 2;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final float scale = metrics.density;
        int pixels70 = (int) (dip70 * scale + 0.5f);
        int pixels2 = (int) (dip2 * scale + 0.5f);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setBackgroundColor(Color.RED);
        LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,pixels70);
        LLParams.setMargins(pixels2, pixels2, pixels2, pixels2);
        linearLayout.setLayoutParams(LLParams);

        ImageView imageView = new ImageView(this);
        imageView.setBackgroundColor(Color.WHITE);
        imageView.setImageResource(R.drawable.ic_window);
        LinearLayout.LayoutParams IVParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f);        imageView.setLayoutParams(IVParams);

        TextView textView_WindowName = new TextView(this);
        textView_WindowName.setBackgroundColor(Color.WHITE);
        textView_WindowName.setTextSize(16);
        textView_WindowName.setText("Elternschlafzimmer");
        textView_WindowName.setGravity(Gravity.CENTER | Gravity.LEFT);
        LinearLayout.LayoutParams TVParams = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT, 3f);
        textView_WindowName.setLayoutParams(TVParams);
        // id="@+id/oxygenText", gravity="center_vertical|left", textAlignment="gravity" />

        TextView textView_WindowStatus = new TextView(this);
        textView_WindowStatus.setBackgroundColor(Color.WHITE);
        textView_WindowStatus.setTextSize(16);
        textView_WindowStatus.setText("open");
        textView_WindowStatus.setGravity(Gravity.CENTER | Gravity.RIGHT);
        textView_WindowStatus.setLayoutParams(TVParams);
        //layout_width="0dip", layout_height="match_parent", layout_weight="3", id="@+id/oxygenOutputText", gravity="center_vertical|right", textAlignment="gravity" />

        ImageView imageView_Arrow = new ImageView(this);
        imageView_Arrow.setBackgroundColor(Color.WHITE);
        imageView_Arrow.setImageResource(R.drawable.ic_next_page);
        imageView_Arrow.setLayoutParams(IVParams);
        //layout_weight="1"


        LinearLayout ll = (LinearLayout)findViewById(R.id.windowButtonLayout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.addView(linearLayout);
        linearLayout.addView(imageView);
        linearLayout.addView(textView_WindowName);
        linearLayout.addView(textView_WindowStatus);
        linearLayout.addView(imageView_Arrow);
    }

}
