package com.safeandsound.app.safeandsound.view;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.safeandsound.app.safeandsound.AppConfig;
import com.safeandsound.app.safeandsound.R;

/**
 * Created by Dominic on 19.05.2017.
 */

public class MotionPictureActivity extends Activity{

    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {

        int id = 1;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.livestream);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
           id = bundle.getInt("id");
        }
        webView  = (WebView)findViewById(R.id.webView2);
        //setzte den Webviw auf die größe vom smartphone
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        //Motion muss auf dem Raspberry laufen, damit der Livestream angesehen werden kann.

        //get the URL of the motion Picture, show saved Picture when motion was detected
        String url = AppConfig.URL_MOTION_PICTURE;
        url = url + id;
        //access motionPicture and show it in a webView
        webView.loadUrl(url);
    }

    //'Zurück' Navigation schließt die Verbindung und den View
    public void onPause(){
        super.onPause();
        webView.destroy();
    }
}
