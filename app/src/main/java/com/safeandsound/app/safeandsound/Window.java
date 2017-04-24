package com.safeandsound.app.safeandsound;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

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

        //Damit die hinzugefügten Elemente persistent werden
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Set<String> imageViewSet = prefs.getStringSet("saved_imageViews",null);
        Set<String> textViewSet = prefs.getStringSet("saved_textViews",null);
        Set<String> layoutSet = prefs.getStringSet("saved_layouts",null);
        if(imageViewSet == null){
            imageViewSet = new HashSet<String>();
        }
        if(textViewSet == null){
            textViewSet = new HashSet<String>();
        }
        if(layoutSet == null){
            layoutSet = new HashSet<String>();
        }

        //Umrechnung von gewünschten dip zu pixels des Handys
        int dip70 = 70;
        int dip2 = 2;
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final float scale = metrics.density;
        int pixels70 = (int) (dip70 * scale + 0.5f);
        int pixels2 = (int) (dip2 * scale + 0.5f);

        //Initialisierung des LinearLayouts des neuen Windows
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setBackgroundColor(Color.RED);
        LinearLayout.LayoutParams LLParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,pixels70);
        LLParams.setMargins(pixels2, pixels2, pixels2, pixels2);
        linearLayout.setLayoutParams(LLParams);
        layoutSet.add("LinearLayout");

        //Initialisierung des ImageView mit Window-Icon des neuen Windows
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundColor(Color.WHITE);
        imageView.setImageResource(R.drawable.ic_window);
        LinearLayout.LayoutParams IVParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f);        imageView.setLayoutParams(IVParams);
        imageViewSet.add("ImageView_WindowIcon");

        //Initialisierung des TextView mit Area/Sensor Name des neuen Windows
        TextView textView_WindowName = new TextView(this);
        textView_WindowName.setBackgroundColor(Color.WHITE);
        textView_WindowName.setTextSize(16);
        textView_WindowName.setText("Elternschlafzimmer");
        textView_WindowName.setGravity(Gravity.CENTER | Gravity.LEFT);
        LinearLayout.LayoutParams TVParams = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT, 3f);
        textView_WindowName.setLayoutParams(TVParams);
        textViewSet.add("TextView_SensorName");
        // id="@+id/oxygenText", gravity="center_vertical|left", textAlignment="gravity" />

        //Initialisierung des TextView mit Sensor Status des neuen Windows
        TextView textView_WindowStatus = new TextView(this);
        textView_WindowStatus.setBackgroundColor(Color.WHITE);
        textView_WindowStatus.setTextSize(16);
        textView_WindowStatus.setText("open");
        textView_WindowStatus.setGravity(Gravity.CENTER | Gravity.RIGHT);
        textView_WindowStatus.setLayoutParams(TVParams);
        textViewSet.add("TextView_SensorStatus");
        //layout_width="0dip", layout_height="match_parent", layout_weight="3",
        // id="@+id/oxygenOutputText"

        //Initialisierung des ImageView mit NextPage-Icon des neuen Windows
        ImageView imageView_Arrow = new ImageView(this);
        imageView_Arrow.setBackgroundColor(Color.WHITE);
        imageView_Arrow.setImageResource(R.drawable.ic_next_page);
        imageView_Arrow.setLayoutParams(IVParams);
        imageViewSet.add("ImageView_NextPageIcon");
        //layout_weight="1"

        //Hinzufügen des LinearLayouts, den zwei TextViews und den zwei ImageViews zu dem default
        // erstellten LinearLayouts windowButtonLayout
        LinearLayout ll = (LinearLayout)findViewById(R.id.windowButtonLayout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.addView(linearLayout);
        linearLayout.addView(imageView);
        linearLayout.addView(textView_WindowName);
        linearLayout.addView(textView_WindowStatus);
        linearLayout.addView(imageView_Arrow);

        prefs.edit().putStringSet("saved_imageViews", imageViewSet).commit();
        prefs.edit().putStringSet("saved_textViews", textViewSet).commit();
        prefs.edit().putStringSet("saved_layouts", layoutSet).commit();
    }

}
