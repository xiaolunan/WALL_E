package com.example.renchunlin.wall_e.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.renchunlin.wall_e.R;
import com.example.renchunlin.wall_e.entity.EncounterData;
import com.example.renchunlin.wall_e.utils.PicassoUtils;

import java.util.List;

/**
 * 项目名：   WALL_E
 * 包名：     com.example.renchunlin.wall_e.ui;
 * 文件名：   WeChatAdapter
 * 创建者：   RCL
 * 创建时间： 2016/10/28 22:51
 * 描述：     偶遇的适配器
 */
public class EncounterAdapter extends BaseAdapter{

    private Context mContext;
    private List<EncounterData> mList;
    private EncounterData data;
    private LayoutInflater inflater;
    private int width,height;
    private WindowManager wm;

    public EncounterAdapter(Context mContext,List<EncounterData> mList){
        this.mContext=mContext;
        this.mList=mList;

        this.inflater=LayoutInflater.from(mContext);

        inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        wm= (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width=wm.getDefaultDisplay().getWidth();
        height=wm.getDefaultDisplay().getHeight();
    }

    public void onDateChange(List<EncounterData> mList) {
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
            convertView=inflater.inflate(R.layout.encounter_item,null);
            viewHolder.iv_img= (ImageView) convertView.findViewById(R.id.iv_img);
            viewHolder.tv_title= (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_source= (TextView) convertView.findViewById(R.id.tv_source);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        data=mList.get(position);
        viewHolder.tv_title.setText(data.getTitle());
        viewHolder.tv_source.setText(data.getDescription());
        //加载图片
        PicassoUtils.loadImageViewSize(mContext,data.getPicUrl(),width/3,200,viewHolder.iv_img);
        return convertView;
    }

    class ViewHolder{
        private ImageView iv_img;
        private TextView tv_title,tv_source;
    }
}
