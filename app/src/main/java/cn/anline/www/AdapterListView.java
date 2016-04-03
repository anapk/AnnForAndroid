package cn.anline.www;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/4/3.
 */
public class AdapterListView extends BaseAdapter {
    private Context context;
    private String[] mTitle;
    private int[] mPic ;
    private String[] mDesc;

    public AdapterListView(Context context,String[] mTitle,int[] mPic,String[] mDesc){
        this.context =context;
        this.mTitle =mTitle;
        this.mPic=mPic;
        this.mDesc =mDesc;
    }

    @Override
    public int getCount() {
        return mTitle.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        HolderView holderView=null;
        if (convertView==null) {
            holderView=new HolderView();
            convertView= LayoutInflater.from(context).inflate(R.layout.zone_listview, null);
            holderView.my_pic=(ImageView) convertView.findViewById(R.id.myPic);
            holderView.my_t1=(TextView) convertView.findViewById(R.id.myT1);
            holderView.my_t2=(TextView) convertView.findViewById(R.id.myT2);
            convertView.setTag(holderView);
        }else {
            holderView=(HolderView) convertView.getTag();
        }


        holderView.my_pic.setImageResource(mPic[position]);
        holderView.my_t1.setText(mTitle[position]);
        holderView.my_t2.setText(mDesc[position]);


        return convertView;
    }


    public class HolderView {

        private ImageView my_pic;
        private TextView my_t1;
        private TextView my_t2;

    }
}
