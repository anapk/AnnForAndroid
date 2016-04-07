package cn.anline.www.qrcode;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.anline.www.R;
import cn.anline.www.wxapi.Constants;
import cn.anline.www.wxapi.Util;


public class ResultActivity extends Activity implements IWXAPIEventHandler,IUiListener {
    private  String qqAPP_ID = "1105137195";
    public static Tencent mTencent;
    private long exitTime = 0;
    private ImageView qrcode_web_back,qrcode_web_forward;
    private ImageView qrcode_web_share_qq,qrcode_web_share_qzone,qrcode_web_share_wechat,qrcode_web_share_timeline,qrcode_web_share_weibo;
    private IWXAPI api;
    private WebView webView;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_qrcode_result);
//以下创建并注册微信接口
        api = WXAPIFactory.createWXAPI(this, Constants.wxAPP_ID, false);
        api.registerApp(Constants.wxAPP_ID);
//以下注册到QQ开放平台接口
        mTencent = Tencent.createInstance(qqAPP_ID, this.getApplicationContext());
        setContentView(R.layout.activity_qrcode_web);
        extras = getIntent().getExtras();
        webView = (WebView) findViewById(R.id.webView_qrweb);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        qrcode_web_back             = (ImageView) findViewById(R.id.qrcode_web_back);
        qrcode_web_forward          = (ImageView) findViewById(R.id.qrcode_web_forward);
        qrcode_web_share_qq         = (ImageView) findViewById(R.id.share_qq);
        qrcode_web_share_qzone       = (ImageView) findViewById(R.id.share_qzone);
        qrcode_web_share_wechat     = (ImageView) findViewById(R.id.share_wechat);
        qrcode_web_share_timeline  = (ImageView) findViewById(R.id.share_timeline);
        qrcode_web_share_weibo      = (ImageView) findViewById(R.id.share_weibo);


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
            qrcode_web_back.setOnClickListener(new QRCodeOnClickListener());
            qrcode_web_forward.setOnClickListener(new QRCodeOnClickListener());
            qrcode_web_share_qq.setOnClickListener(new QRCodeOnClickListener());
            qrcode_web_share_qzone.setOnClickListener(new QRCodeOnClickListener());
            qrcode_web_share_wechat.setOnClickListener(new QRCodeOnClickListener());
            qrcode_web_share_timeline.setOnClickListener(new QRCodeOnClickListener());
            qrcode_web_share_weibo.setOnClickListener(new QRCodeOnClickListener());

            api.handleIntent(getIntent(),this);
        }
    }
//以下实现实例微信接口
    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {

    }
//以下实现实例QQ接口
@Override
public void onComplete(Object o) {
    Toast.makeText(getApplicationContext(),"QQ分享成功",Toast.LENGTH_SHORT).show();
}

    @Override
    public void onError(UiError uiError) {
        Toast.makeText(getApplicationContext(),"QQ分享错误",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel() {
        Toast.makeText(getApplicationContext(),"您取消了QQ分享",Toast.LENGTH_SHORT).show();
    }
    //    QQ分享结果监听
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != mTencent)
            mTencent.onActivityResult(requestCode, resultCode, data);
    }
    protected class QRCodeOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.qrcode_web_back:

                    break;
                case R.id.qrcode_web_forward:

                    break;
                case R.id.share_qq:
                    shareToQQ();

                    break;
                case R.id.share_qzone:
                    shareToQZone();

                    break;
                case R.id.share_wechat:
                    WXWebpageObject webpage = new WXWebpageObject();
                    webpage.webpageUrl = webView.getUrl();
                    WXMediaMessage msg = new WXMediaMessage(webpage);
                    msg.title = webView.getTitle();
                    msg.description = "安浪创想网：从事互联网技术产品开发，专注于信息技术领域的高端商业资源开发，技术型、创新型、公益性网站，欢迎合作、共赢！";
                    Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                    msg.thumbData = Util.bmpToByteArray(thumb, true);

                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = buildTransaction("webpage");
                    req.message = msg;
                    req.scene = SendMessageToWX.Req.WXSceneSession;
                    api.sendReq(req);

                    break;
                case R.id.share_timeline:
                    WXWebpageObject webpage2 = new WXWebpageObject();
                    webpage2.webpageUrl = webView.getUrl();
                    WXMediaMessage msg2 = new WXMediaMessage(webpage2);
                    msg2.title = webView.getTitle();
                    msg2.description = "安浪创想网：从事互联网技术产品开发，专注于信息技术领域的高端商业资源开发，技术型、创新型、公益性网站，欢迎合作、共赢！";
                    Bitmap thumb2 = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                    msg2.thumbData = Util.bmpToByteArray(thumb2, true);

                    SendMessageToWX.Req req2 = new SendMessageToWX.Req();
                    req2.transaction = buildTransaction("webpage");
                    req2.message = msg2;
                    req2.scene = SendMessageToWX.Req.WXSceneTimeline;
                    api.sendReq(req2);

                    break;
                case R.id.share_weibo:

                    break;
                default:

                    break;
            }
        }
    }
    private void shareToQZone(){
        Bundle bundle = new Bundle();

        bundle.putString(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, "安浪APP");

        bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, webView.getUrl());

        bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, webView.getTitle());

        bundle.putString(QzoneShare.SHARE_TO_QQ_IMAGE_URL, Constants.LogoURL);

        bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "安浪创想网：从事互联网技术产品开发，专注于信息技术领域的高端商业资源开发，技术型、创新型、公益性网站，欢迎合作、共赢！");

        bundle.putString(QzoneShare.SHARE_TO_QQ_SITE, "安浪创想网");

        bundle.putString(QzoneShare.SHARE_TO_QQ_APP_NAME, "安浪创想");

        mTencent.shareToQzone(this, bundle, this);
    }
    private void shareToQQ(){
        Bundle bundle = new Bundle();

        bundle.putString(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, "");

        bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, webView.getUrl());

        bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, webView.getTitle());

        bundle.putString(QzoneShare.SHARE_TO_QQ_IMAGE_URL, Constants.LogoURL);

        bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "安浪创想网：从事互联网技术产品开发，专注于信息技术领域的高端商业资源开发，技术型、创新型、公益性网站，欢迎合作、共赢！");

        bundle.putString(QzoneShare.SHARE_TO_QQ_SITE, "安浪创想网");

        bundle.putString(QzoneShare.SHARE_TO_QQ_APP_NAME, "安浪创想");

        mTencent.shareToQQ(this, bundle, this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出浏览", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                ResultActivity.this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
