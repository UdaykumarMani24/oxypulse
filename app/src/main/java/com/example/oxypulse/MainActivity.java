package com.example.oxypulse;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{

    WebView myWebView;
    private ValueCallback<Uri[]> mFilePathCallback;
    private static final int FILE_CHOOSER_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myWebView = findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        // Set WebViewClient to ensure navigation happens within the WebView
        myWebView.setWebViewClient(new WebViewClient());

        // Set WebChromeClient to handle file chooser (for "Browse" buttons)
        myWebView.setWebChromeClient(new WebChromeClient() {
            // For Android 5.0 and above
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                if (mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(null); // Reset previous callback
                }
                mFilePathCallback = filePathCallback;

                // Create the file chooser intent
                Intent intent = fileChooserParams.createIntent();
                try {
                    startActivityForResult(intent, FILE_CHOOSER_REQUEST_CODE);
                } catch (android.content.ActivityNotFoundException e) {
                    Toast.makeText(MainActivity.this, "Cannot open file chooser", Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }
        });

        // Load the external URL in the WebView
        myWebView.loadUrl("https://healthbin-zkyfxdw2z9hhl58cskquhu.streamlit.app/");
       // myWebView.loadUrl("https://healthbin-zkyfxdw2z9hhl58cskquhu.streamlit.app/");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == FILE_CHOOSER_REQUEST_CODE) {
            if (mFilePathCallback == null) return;
            Uri[] result = WebChromeClient.FileChooserParams.parseResult(resultCode, intent);
            mFilePathCallback.onReceiveValue(result);
            mFilePathCallback = null;
        } else {
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }

    @Override
    public void onBackPressed() {
        if (myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
