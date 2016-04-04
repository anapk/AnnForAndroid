package cn.anline.www.qrcode;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.anline.www.R;


public class ResultActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_qrcode_result);


        setContentView(R.layout.activity_qrcode_web);
        Bundle extras = getIntent().getExtras();
        WebView webView = (WebView) findViewById(R.id.webView_qrweb);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });



        if (extras != null) {
            String result = extras.getString("result");
            webView.loadUrl(result);
/*
            Pattern pattern=Pattern.compile("http://(([a-zA-z0-9]|-){1,}\\.){1,}[a-zA-z0-9]{1,}-*");
            Matcher matcher=pattern.matcher(result);
            Pattern pattern2=Pattern.compile("https://(([a-zA-z0-9]|-){1,}\\.){1,}[a-zA-z0-9]{1,}-*");
            Matcher matcher2=pattern2.matcher(result);
            if(matcher.find()||matcher2.find()){
//                System.out.print(result + "扫描结果为网址！");
                Toast.makeText(getApplicationContext(),"扫描结果为链接网址："+result,Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),"正在为您加载："+result,Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),"加载速度取决于网络访问速度，请您耐心等候",Toast.LENGTH_LONG).show();
                Intent rsWeb = new Intent(ResultActivity.this, QrWebActivity.class);
                rsWeb.putExtra("QRUrl",result);
                startActivity(rsWeb);
            }else{
                Intent rsQrtextShow =new Intent(ResultActivity.this,ResultTextShowActivity.class);
                rsQrtextShow.putExtra("rsText",result);
                Toast.makeText(getApplicationContext(),"扫描结果为文本信息",Toast.LENGTH_SHORT).show();
                startActivity(rsQrtextShow);
            }
*/

//            如果没有获得扫描结果的值

        }
    }


}
