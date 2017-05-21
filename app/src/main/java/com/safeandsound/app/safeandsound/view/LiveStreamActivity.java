package com.safeandsound.app.safeandsound.view;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import com.safeandsound.app.safeandsound.AppConfig;
import com.safeandsound.app.safeandsound.R;



/**
 * Created by Dominic on 27.04.2017.
 */

public class LiveStreamActivity extends Activity {

    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.livestream);

        webView  = (WebView)findViewById(R.id.webView2);
        //setzte den Webviw auf die größe vom smartphone
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        //Motion muss auf dem Raspberry laufen, damit der Livestream angesehen werden kann.

        //get the URL of the Livestream
        String urlLivestream = AppConfig.URL_LIVESTREAM;

        //access livestream and show it in a webView
        webView.loadUrl(urlLivestream);


    }


    //'Zurück' Navigation schließt die Verbindung und den View
    public void onPause(){
        super.onPause();
        webView.destroy();
    }


}
