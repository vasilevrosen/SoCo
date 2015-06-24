package com.soco.ebusiness.soco;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by steffenteichmann on 24.06.15.
 * Erstellt den Wikipediaartikel anch Thema
 */
public class activity_wiki_article extends Activity {
    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wiki_activity);
        Bundle i = getIntent().getExtras();
        String uri = i.getString("URI");
        webView = (WebView) findViewById(R.id.wiki_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(uri);

    }
}
