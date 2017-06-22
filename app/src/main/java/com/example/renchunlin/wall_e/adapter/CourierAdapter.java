package com.example.renchunlin.wall_e.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.renchunlin.wall_e.R;
import com.example.renchunlin.wall_e.entity.CourierData;
import java.util.List;

/*
 *  项目名:     WALL_E
 *  包名:       com.example.renchunlin.wall_e.ui;
 *  文件名:     CourierAdapter
 *  创建者:     RCL
 *  创建时间:   2016/9/4 16:43
 *  描述:       快递查询适配器
 */
public class CourierAdapter extends BaseAdapter {

    private Context mContext;
    private List<CourierData> mList;
    //布局加载器
    private LayoutInflater inflater;
    private CourierData data;

    public CourierAdapter(Context mContext,List<CourierData> mList){
        this.mContext=mContext;
        this.mList=mList;
        //获取系统服务
        inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        //第一次加载
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.layout_courier_item,null);
            viewHolder.tv_content= (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.tv_time= (TextView) convertView.findViewById(R.id.tv_time);
            //设置缓存
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        //设置数据
        data=mList.get(position);
        viewHolder.tv_content.setText(data.getContent());
        viewHolder.tv_time.setText(data.getTime());

        return convertView;
    }

    class ViewHolder{
        private TextView tv_content,tv_time;
    }
}
