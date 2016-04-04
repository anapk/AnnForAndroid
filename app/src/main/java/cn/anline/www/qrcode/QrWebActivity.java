package cn.anline.www.qrcode;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.anline.www.R;

public class QrWebActivity extends Activity {
    Bundle extras2 = getIntent().getExtras();
    WebView webView;
    String rsUrlValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_web);
        webView = (WebView) findViewById(R.id.webView_qrweb);
        rsUrlValue = extras2.getString("QRUrl");
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webView.loadUrl(rsUrlValue);
    }
}
