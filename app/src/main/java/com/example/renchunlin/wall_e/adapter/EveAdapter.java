package com.example.renchunlin.wall_e.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.renchunlin.wall_e.R;
import com.example.renchunlin.wall_e.entity.EveData;
import com.example.renchunlin.wall_e.utils.PicassoUtils;

import java.util.List;

/**
 * 项目名：   WALL_E
 * 包名：     com.example.renchunlin.wall_e.ui;
 * 文件名：   EveAdapter
 * 创建者：   RCL
 * 创建时间： 2016/9/6 16:33
 * 描述：     Eve的适配器
 */
public class EveAdapter extends BaseAdapter{

    private Context mContext;
    private List<EveData> mList;
    private EveData data;
    private LayoutInflater inflater;

    private WindowManager wm;
    //屏幕宽
    private int width;

    public EveAdapter(Context mContext,List<EveData> mList){
        this.mContext=mContext;
        this.mList=mList;

        this.inflater=LayoutInflater.from(mContext);

        inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        wm= (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width=wm.getDefaultDisplay().getWidth();
    }

    public void onDateChange(List<EveData> mList) {
        this.mList = mList;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.eve_item,null);
            viewHolder.imageView= (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        data=mList.get(position);
        //解析图片
        String url=data.getImgUrl();
        PicassoUtils.loadImageViewSize(mContext,url,width/2,700,viewHolder.imageView);

        return convertView;
    }

    class ViewHolder{
        private ImageView imageView;
    }
}
