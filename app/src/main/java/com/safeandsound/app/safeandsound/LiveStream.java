package com.safeandsound.app.safeandsound;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

/**
 * Created by Dominic on 27.04.2017.
 */

public class LiveStream extends Activity {

    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.livestream);

        webView  = (WebView)findViewById(R.id.webView2);
        //Motion muss auf dem Raspberry laufen, damit der Livestream angesehen werden kann.
        webView.loadUrl("http://192.168.2.103:8081");
    }


}
