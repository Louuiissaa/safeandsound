package com.safeandsound.app.safeandsound;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Created by louisapabst on 16.04.17.
 */

public class Room extends FragmentActivity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.air_quality);
    }
}
