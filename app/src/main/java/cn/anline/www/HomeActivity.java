package cn.anline.www;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends Activity {
    private ViewPager mTabPager;
    private ImageView mTabAnn,mTabService,mTabFind,mTabBiz,mTabZone;
    private RelativeLayout layout;
//    private ListView zoneListView;
//    private AdapterListView adapterListView;

    String[] aTitle = {
            "安浪1",
            "安浪2",
            "安浪3",
            "安浪4",
            "安浪5",
            "安浪6",
            "安浪7",
            "安浪8",
            "安浪9",
            "安浪10",
            "安浪11",
            "安浪12",
            "安浪13",
            "安浪14",
            "安浪15",
            "安浪16",
            "安浪17",
            "安浪18",
            "安浪19",
            "安浪20"
    };
    int[] aPic ={
            R.drawable.m1,
            R.drawable.m2,
            R.drawable.m3,
            R.drawable.m4,
            R.drawable.m5,
            R.drawable.m6,
            R.drawable.m7,
            R.drawable.m8,
            R.drawable.m9,
            R.drawable.m10,
            R.drawable.m11,
            R.drawable.m12,
            R.drawable.m13,
            R.drawable.m14,
            R.drawable.m15,
            R.drawable.m16,
            R.drawable.m17,
            R.drawable.m18,
            R.drawable.m19,
            R.drawable.m20

    };
    String[] aDesc ={
            "1网站建设，网站开发，域名注册，主机开通，服务器运营维护",
            "2网络营销，产品视觉设计，产品渠道，销售管理系统，在线支付",
            "3网贷系统，理财系统，信用卡服务，外贸货币",
            "4在线编程，视频讲课，远程教育，课程系统，在线考试",
            "5智能家居，室内环保，家用电器，装修设计，地产中介服务",
            "6汽车销售，汽车修理，汽车装饰，二手车交易，租车",
            "7大众创新，万众创业，艰苦奋斗，融资合伙",
            "8物联网硬件，串口程序，控制系统，机器人开发",
            "9地区特产，绿色农产品，在线农场，生鲜果蔬和肉类",
            "10网站建设，网站开发，域名注册，主机开通，服务器运营维护",
            "11网络营销，产品视觉设计，产品渠道，销售管理系统，在线支付",
            "12网贷系统，理财系统，信用卡服务，外贸货币",
            "13在线编程，视频讲课，远程教育，课程系统，在线考试",
            "14智能家居，室内环保，家用电器，装修设计，地产中介服务",
            "15汽车销售，汽车修理，汽车装饰，二手车交易，租车",
            "16大众创新，万众创业，艰苦奋斗，融资合伙",
            "17物联网硬件，串口程序，控制系统，机器人开发",
            "18地区特产，绿色农产品，在线农场，生鲜果蔬和肉类",
            "19物联网硬件，串口程序，控制系统，机器人开发",
            "20地区特产，绿色农产品，在线农场，生鲜果蔬和肉类"
    };
    ArrayList<Map<String,Object>> aData = new ArrayList<Map<String,Object>>();
    private int currIndex = 0;// 当前页卡编号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        layout = (RelativeLayout) findViewById(R.id.mainweixin);

        mTabPager = (ViewPager)findViewById(R.id.tabpager);
        mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());

        mTabAnn = (ImageView) findViewById(R.id.tab_img_ann);
        mTabService = (ImageView) findViewById(R.id.tab_img_service);
        mTabFind = (ImageView) findViewById(R.id.tab_img_find);
        mTabBiz = (ImageView) findViewById(R.id.tab_img_biz);
        mTabZone = (ImageView) findViewById(R.id.tab_img_zone);


        mTabAnn .setOnClickListener(new MyOnClickListener(0));
        mTabService.setOnClickListener(new MyOnClickListener(1));
        mTabFind.setOnClickListener(new MyOnClickListener(2));
        mTabBiz.setOnClickListener(new MyOnClickListener(3));
        mTabZone.setOnClickListener(new MyOnClickListener(4));

        //将要分页显示的View装入数组中
        LayoutInflater mLi = LayoutInflater.from(this);

        View viewAnn = mLi.inflate(R.layout.activity_ann, null);
        View viewService = mLi.inflate(R.layout.activity_service, null);
        View viewFind = mLi.inflate(R.layout.activity_find, null);
        View viewBiz = mLi.inflate(R.layout.activity_biz, null);
        View viewZone = mLi.inflate(R.layout.activity_zone, null);

        //每个页面的view数据
        final ArrayList<View> views = new ArrayList<View>();
        views.add(viewAnn);
        views.add(viewService);
        views.add(viewFind);
        views.add(viewBiz);
        views.add(viewZone);
        //填充ViewPager的数据适配器
        PagerAdapter mPagerAdapter = new PagerAdapter()
        {
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public void destroyItem(View container, int position, Object object) {
                ((ViewPager)container).removeView(views.get(position));
            }

            @Override
            public Object instantiateItem(View container, int position) {
                ((ViewPager)container).addView(views.get(position));
                return views.get(position);
            }
        };
        mTabPager.setAdapter(mPagerAdapter);
//        zoneListView = (ListView) findViewById(R.id.listView);
//        int lengh = aTitle.length;
//        for (int i=0;i<lengh;i++){
//            Map<String,Object> item = new HashMap<String, Object>();
//            item.put("title",aTitle[i]);
//            item.put("pic",aPic[i]);
//            item.put("desc",aDesc[i]);
//            aData.add(item);
//        }

//        SimpleAdapter adapter = new SimpleAdapter(this,aData,R.layout.zone_listview,new String[]{"title","pic","desc"},new int[]{R.id.myT1,R.id.myPic,R.id.myT2});

    }
    public void setLayoutBg(int annn){
        switch (annn) {
            case 0:
                layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.ann_bg));
                break;
            case  1:
                layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.service_bg));
                break;
            case 2:
                layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.find_bg));
                break;
            case 3:
                layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.biz_bg));
                break;
            case 4:
                layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.zone_bg));
                break;
            default:
                layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.anbg));
                break;
        }

    }
    /**
     * 头标点击监听
     */
    public class MyOnClickListener implements View.OnClickListener
    {
        private int index = 0;
        public MyOnClickListener(int i)
        {
            index = i;
        }
        @Override
        public void onClick(View v)
        {
            mTabPager.setCurrentItem(index);
        }
    };

    /**
     * 页卡切换监听
     */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener
    {
        @Override
        public void onPageSelected(int arg0)
        {
            switch (arg0)
            {
                case 0:
                {
                    mTabAnn.setImageDrawable(getResources().getDrawable(R.drawable.annpress));
//                    setLayoutBg(0);
                    if (currIndex == 1)
                    {
                        mTabService.setImageDrawable(getResources().getDrawable(R.drawable.service));
                    }
                    else if (currIndex == 2)
                    {
                        mTabFind.setImageDrawable(getResources().getDrawable(R.drawable.find));
                    }
                    else if (currIndex == 3)
                    {
                        mTabBiz.setImageDrawable(getResources().getDrawable(R.drawable.biz));
                    }
                    else if (currIndex == 4)
                    {
                        mTabZone.setImageDrawable(getResources().getDrawable(R.drawable.zone));
                    }
                    break;
                }
                case 1:
                {
                    mTabService.setImageDrawable(getResources().getDrawable(R.drawable.servicepress));
//                    setLayoutBg(1);
                    if (currIndex == 0)
                    {
                        mTabAnn.setImageDrawable(getResources().getDrawable(R.drawable.ann));
                    }
                    else if (currIndex == 2)
                    {
                        mTabFind.setImageDrawable(getResources().getDrawable(R.drawable.find));
                    }
                    else if (currIndex == 3)
                    {
                        mTabBiz.setImageDrawable(getResources().getDrawable(R.drawable.biz));
                    }
                    else if (currIndex == 4)
                    {
                        mTabZone.setImageDrawable(getResources().getDrawable(R.drawable.zone));
                    }
                    break;
                }
                case 2:
                {
                    mTabFind.setImageDrawable(getResources().getDrawable(R.drawable.findpress));
//                    setLayoutBg(2);
                    if (currIndex == 0)
                    {
                        mTabAnn.setImageDrawable(getResources().getDrawable(R.drawable.ann));
                    }
                    else if (currIndex == 1)
                    {
                        mTabService.setImageDrawable(getResources().getDrawable(R.drawable.service));
                    }
                    else if (currIndex == 3)
                    {
                        mTabBiz.setImageDrawable(getResources().getDrawable(R.drawable.biz));
                    }
                    else if (currIndex == 4)
                    {
                        mTabZone.setImageDrawable(getResources().getDrawable(R.drawable.zone));
                    }
                    break;
                }
                case 3:
                {
                    mTabBiz.setImageDrawable(getResources().getDrawable(R.drawable.bizpress));
//                    setLayoutBg(3);
                    if (currIndex == 0)
                    {
                        mTabAnn.setImageDrawable(getResources().getDrawable(R.drawable.ann));
                    }
                    else if (currIndex == 2)
                    {
                        mTabFind.setImageDrawable(getResources().getDrawable(R.drawable.find));
                    }
                    else if (currIndex == 1)
                    {
                        mTabService.setImageDrawable(getResources().getDrawable(R.drawable.service));
                    }
                    else if (currIndex == 4)
                    {
                        mTabZone.setImageDrawable(getResources().getDrawable(R.drawable.zone));
                    }
                    break;
                }
                case 4:
                {
                    mTabZone.setImageDrawable(getResources().getDrawable(R.drawable.zonepress));
//                    setLayoutBg(4);
//                    zoneListView.setSelector(new ColorDrawable(Color.BLUE));
//                    adapterListView = new AdapterListView(getApplicationContext(),aTitle,aPic,aDesc);
//                    zoneListView.setAdapter(adapterListView);
//                    zoneListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            Toast.makeText(getApplicationContext(), "您点击了" + aTitle[position], Toast.LENGTH_LONG).show();
//                        }
//                    });
                    if (currIndex == 0)
                    {
                        mTabAnn.setImageDrawable(getResources().getDrawable(R.drawable.ann));
                    }
                    else if (currIndex == 2)
                    {
                        mTabFind.setImageDrawable(getResources().getDrawable(R.drawable.find));
                    }
                    else if (currIndex == 3)
                    {
                        mTabBiz.setImageDrawable(getResources().getDrawable(R.drawable.biz));
                    }
                    else if (currIndex == 1)
                    {
                        mTabService.setImageDrawable(getResources().getDrawable(R.drawable.service));
                    }
                    break;
                }
            }
            currIndex = arg0;
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("确定退出程序吗？")
                .setIcon(R.drawable.waring)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“确认”后的操作
                        HomeActivity.this.finish();

                    }
                })
                .setMessage("确定后将关闭安浪创想APP。\n非常感谢您对安浪的支持！")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作
                    }
                }).show();
    }
}