package cn.anline.www.tools;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.connect.share.QzonePublish;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

import cn.anline.www.R;
import cn.anline.www.openqq.Util;
import cn.anline.www.wxapi.Constants;

public class ZbQZoneActivity extends Activity implements View.OnClickListener,IWXAPIEventHandler {
    private LinearLayout mImageListLayout = null;
    private long exitTime = 0;
    private IWXAPI api;
    public static Tencent mTencent;
    private ImageView addImg,zbDelete,zbQzone,zbWechat,zbTimeline,zbWeibo;
    private EditText summary = null;
    private LinearLayout mImageContainerLayout = null;
    //QZone分享， SHARE_TO_QQ_TYPE_DEFAULT 图文，SHARE_TO_QQ_TYPE_IMAGE 纯图
    private int shareType = QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zb_tool);
//下面的创建并注册微信api
        api = WXAPIFactory.createWXAPI(this, Constants.wxAPP_ID, false);
        api.registerApp(Constants.wxAPP_ID);
//以下注册到QQ开放平台接口
        mTencent = Tencent.createInstance(Constants.qqAPP_ID, this.getApplicationContext());

        mImageListLayout = (LinearLayout)findViewById(R.id.images_picker_layout);
        mImageContainerLayout = (LinearLayout) findViewById(R.id.qqshare_imageUrl_container);

        addImg = (ImageView) findViewById(R.id.btn_addImage);
        zbDelete = (ImageView) findViewById(R.id.ann_zb_iv_delete);
        zbQzone = (ImageView) findViewById(R.id.ann_zb_iv_qzone);
        zbWechat = (ImageView) findViewById(R.id.ann_zb_iv_wechat);
        zbTimeline = (ImageView) findViewById(R.id.ann_zb_iv_timeline);
        zbWeibo = (ImageView) findViewById(R.id.ann_zb_iv_weibo);

        summary = (EditText) findViewById(R.id.tv_zb_text);
        addImg.setOnClickListener(this);
        zbDelete.setOnClickListener(this);
        zbQzone.setOnClickListener(this);
        zbWechat.setOnClickListener(this);
        zbTimeline.setOnClickListener(this);
        zbWeibo.setOnClickListener(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出浏览", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                ZbQZoneActivity.this.finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ann_zb_iv_delete:{
                summary.setText("");

            }
            break;
            case R.id.ann_zb_iv_qzone:{
                Bundle bundle = new Bundle();

//                bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, "");

                bundle.putInt(QzonePublish.PUBLISH_TO_QZONE_KEY_TYPE, QzonePublish.PUBLISH_TO_QZONE_TYPE_PUBLISHMOOD);

                bundle.putString(QzonePublish.PUBLISH_TO_QZONE_SUMMARY, summary.getText().toString());

                bundle.putString(QzonePublish.PUBLISH_TO_QZONE_APP_NAME,"安浪创想");
//                bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "");

                ArrayList<String> imageUrls = new ArrayList<String>();
                for (int i = 0; i < mImageListLayout.getChildCount(); i++) {
                    LinearLayout addItem = (LinearLayout) mImageListLayout.getChildAt(i);
                    EditText editText = (EditText) addItem.getChildAt(1);
                    imageUrls.add(editText.getText().toString());
                }

                bundle.putStringArrayList(QzonePublish.PUBLISH_TO_QZONE_IMAGE_URL, imageUrls);

//                bundle.putString(QzoneShare.SHARE_TO_QQ_SITE, "安浪创想网");

//                bundle.putString(QzoneShare.SHARE_TO_QQ_APP_NAME, "安浪创想");

                mTencent.publishToQzone(this,bundle,qZoneShareListener);

            }
            break;
            case R.id.ann_zb_iv_wechat:{
                // 初始化一个WXTextObject对象
                WXTextObject textObj = new WXTextObject();
                textObj.text = summary.getText().toString();

                // 用WXTextObject对象初始化一个WXMediaMessage对象
                WXMediaMessage msg = new WXMediaMessage();
                msg.mediaObject = textObj;
                // 发送文本类型的消息时，title字段不起作用
                // msg.title = "Will be ignored";
                msg.description = summary.getText().toString();

                // 构造一个Req
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
                req.message = msg;
                req.scene = SendMessageToWX.Req.WXSceneSession;

                // 调用api接口发送数据到微信
                api.sendReq(req);
            }
            break;
            case R.id.ann_zb_iv_timeline:{
                // 初始化一个WXTextObject对象
                WXTextObject textObj = new WXTextObject();
                textObj.text = summary.getText().toString();

                // 用WXTextObject对象初始化一个WXMediaMessage对象
                WXMediaMessage msg = new WXMediaMessage();
                msg.mediaObject = textObj;
                // 发送文本类型的消息时，title字段不起作用
                // msg.title = "Will be ignored";
                msg.description = summary.getText().toString();

                // 构造一个Req
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
                req.message = msg;
                req.scene = SendMessageToWX.Req.WXSceneTimeline;

                // 调用api接口发送数据到微信
                api.sendReq(req);

            }
            break;
            case R.id.ann_zb_iv_weibo:{

            }
            break;

            case R.id.btn_addImage:{
                int num = mImageListLayout.getChildCount();
                //         if (num < MAX_IMAGE) {
                LinearLayout addItem = (LinearLayout) LayoutInflater.from(this).inflate(
                        R.layout.zb_tool_image_picker, null);
                mImageListLayout.addView(addItem);
                TextView textView0 = (TextView) addItem.getChildAt(0); // index
                View view1 = addItem.getChildAt(1); // editText url
                View view2 = addItem.getChildAt(2); // picker按钮
                View view3 = addItem.getChildAt(3); // 删除按钮
                textView0.setText(String.valueOf(num + 1));
                view1.setId(1000 + num); // url EditText
                view2.setId(2000 + num); // picker
                view3.setId(3000 + num); // 删除
                addItem.setId(num);
                view2.setOnClickListener(this);
                view3.setOnClickListener(this);

            }
            break;
            default:
                break;
        }
        if (id >= 2000 && id < 3000) {
            // 点的是选择图片
            startPickLocaleImage(this, id - 2000);
        } else if (id >= 3000 && id < 4000) {
            // 点的是删除图片
            if (mImageListLayout.getChildCount() > 0) {
                View view = mImageListLayout.findViewById(id - 3000);
                mImageListLayout.removeView(view);
            }
        }

    }
    private static final void startPickLocaleImage(Activity activity, int requestId) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        if (android.os.Build.VERSION.SDK_INT >= Util.Build_VERSION_KITKAT) {
            intent.setAction(Util.ACTION_OPEN_DOCUMENT);
        } else {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        activity.startActivityForResult(
                Intent.createChooser(intent, activity.getString(R.string.str_image_local)), requestId);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == com.tencent.connect.common.Constants.REQUEST_QZONE_SHARE) {
            Tencent.onActivityResultData(requestCode,resultCode,data,qZoneShareListener);
        }else{
            String path = null;
            if (resultCode == Activity.RESULT_OK) {
                if (data != null && data.getData() != null) {
                    // 根据返回的URI获取对应的SQLite信息
                    Uri uri = data.getData();
                    path = Util.getPath(this, uri);
                }
            }
            if (path != null) {
                // 这里很奇葩的方式, 将获取到的值赋值给相应的EditText, 竟然能对应上
                EditText editText = (EditText)mImageContainerLayout.findViewById(requestCode + 1000);
                editText.setText(path);
            } else {
                showToast("请重新选择图片");
            }
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {

    }
    Toast mToast = null;
    private void showToast(String text) {
        if (mToast != null && !super.isFinishing()) {
            mToast.setText(text);
            mToast.show();
            return;
        }
        mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        mToast.show();
    }
    IUiListener qZoneShareListener = new IUiListener() {

        @Override
        public void onCancel() {
//            Util.toastMessage(ZbQZoneActivity.this, "");

        }

        @Override
        public void onError(UiError e) {
            // TODO Auto-generated method stub
//            Util.toastMessage(ZbQZoneActivity.this, "" + e.errorMessage, "e");

        }

        @Override
        public void onComplete(Object response) {
            // TODO Auto-generated method stub
//            Util.toastMessage(ZbQZoneActivity.this, " " + response.toString());

        }

    };
}
