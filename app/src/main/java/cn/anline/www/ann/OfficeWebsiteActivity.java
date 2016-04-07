package cn.anline.www.ann;

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

import cn.anline.www.R;
import cn.anline.www.wxapi.Constants;
import cn.anline.www.wxapi.Util;

public class OfficeWebsiteActivity extends Activity implements IWXAPIEventHandler,IUiListener {

    private long exitTime = 0;
    private IWXAPI api;
    public static Tencent mTencent;
    private int shareType = QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT;
    private WebView annWebView;
    private ImageView ann_office_web_back,ann_office_web_forward,ann_office_share_qq,ann_office_share_qzone,ann_office_share_wechat,ann_office_share_timeline,ann_office_share_weibo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_website);
//下面的创建并注册微信api
        api = WXAPIFactory.createWXAPI(this, Constants.wxAPP_ID, false);
        api.registerApp(Constants.wxAPP_ID);
//以下注册到QQ开放平台接口
        mTencent = Tencent.createInstance(Constants.qqAPP_ID, this.getApplicationContext());

        annWebView = (WebView) findViewById(R.id.webView_office_website);
        annWebView.getSettings().setJavaScriptEnabled(true);
        annWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        annWebView.loadUrl("http://www.anline.cn");

        ann_office_web_back = (ImageView) findViewById(R.id.ann_office_web_back);
        ann_office_web_forward = (ImageView) findViewById(R.id.ann_office_web_forward);
        ann_office_share_qq = (ImageView) findViewById(R.id.ann_office_share_qq);
        ann_office_share_qzone = (ImageView) findViewById(R.id.ann_office_share_qzone);
        ann_office_share_wechat = (ImageView) findViewById(R.id.ann_office_share_wechat);
        ann_office_share_timeline= (ImageView) findViewById(R.id.ann_office_share_timeline);
        ann_office_share_weibo = (ImageView) findViewById(R.id.ann_office_share_weibo);
        ann_office_web_back.setOnClickListener(new AnnOfficeWebSiteOnClickListener());
        ann_office_web_forward.setOnClickListener(new AnnOfficeWebSiteOnClickListener());
        ann_office_share_qq.setOnClickListener(new AnnOfficeWebSiteOnClickListener());
        ann_office_share_qzone.setOnClickListener(new AnnOfficeWebSiteOnClickListener());
        ann_office_share_wechat.setOnClickListener(new AnnOfficeWebSiteOnClickListener());
        ann_office_share_timeline.setOnClickListener(new AnnOfficeWebSiteOnClickListener());
        ann_office_share_weibo.setOnClickListener(new AnnOfficeWebSiteOnClickListener());

        api.handleIntent(getIntent(), this);

    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {

    }
//下面三个接口实例为QQ开放接口
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

    protected class AnnOfficeWebSiteOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ann_office_web_back:
                    if(annWebView.canGoBack()){
                        annWebView.goBack();
                    }else {
                        Toast.makeText(getApplicationContext(),"不能再后退",Toast.LENGTH_SHORT).show();
                    }

                    break;
                case R.id.ann_office_web_forward:
                    if(annWebView.canGoForward()){
                        annWebView.goForward();
                    }else {
                        Toast.makeText(getApplicationContext(),"不能再前进",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.ann_office_share_qq:
                    shareToQQ();

                    break;
                case R.id.ann_office_share_qzone:
                    shareToQZone();

                    break;
                case R.id.ann_office_share_wechat:
                    WXWebpageObject webpage = new WXWebpageObject();
                    webpage.webpageUrl = annWebView.getUrl();
                    WXMediaMessage msg = new WXMediaMessage(webpage);
                    msg.title = annWebView.getTitle();
                    msg.description = "安浪创想网：从事互联网技术产品开发，专注于信息技术领域的高端商业资源开发，技术型、创新型、公益性网站，欢迎合作、共赢！";
                    Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                    msg.thumbData = Util.bmpToByteArray(thumb, true);

                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = buildTransaction("webpage");
                    req.message = msg;
                    req.scene = SendMessageToWX.Req.WXSceneSession;
                    api.sendReq(req);

                    break;
                case R.id.ann_office_share_timeline:
                    WXWebpageObject webpage2 = new WXWebpageObject();
                    webpage2.webpageUrl = annWebView.getUrl();
                    WXMediaMessage msg2 = new WXMediaMessage(webpage2);
                    msg2.title = annWebView.getTitle();
                    msg2.description = "安浪创想网：从事互联网技术产品开发，专注于信息技术领域的高端商业资源开发，技术型、创新型、公益性网站，欢迎合作、共赢！";
                    Bitmap thumb2 = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                    msg2.thumbData = Util.bmpToByteArray(thumb2, true);

                    SendMessageToWX.Req req2 = new SendMessageToWX.Req();
                    req2.transaction = buildTransaction("webpage");
                    req2.message = msg2;
                    req2.scene = SendMessageToWX.Req.WXSceneTimeline;
                    api.sendReq(req2);

                    break;
                case R.id.ann_office_share_weibo:

                    break;
                default:

                    break;
            }

        }

    }
    private void shareToQZone(){
        Bundle bundle = new Bundle();

        bundle.putString(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, "安浪APP");

        bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, annWebView.getUrl());

        bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, annWebView.getTitle());

        bundle.putString(QzoneShare.SHARE_TO_QQ_IMAGE_URL, Constants.LogoURL);

        bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "安浪创想网：从事互联网技术产品开发，专注于信息技术领域的高端商业资源开发，技术型、创新型、公益性网站，欢迎合作、共赢！");

        bundle.putString(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, "分享的类型");

        bundle.putString(QzoneShare.SHARE_TO_QQ_SITE, "安浪创想网");

        bundle.putString(QzoneShare.SHARE_TO_QQ_APP_NAME, "安浪创想");

        mTencent.shareToQzone(this, bundle, this);
    }
    private void shareToQQ(){
        Bundle bundle = new Bundle();

        bundle.putString(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, "安浪APP");

        bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, annWebView.getUrl());

        bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, annWebView.getTitle());

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
                OfficeWebsiteActivity.this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
