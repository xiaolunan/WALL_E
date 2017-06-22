package com.example.renchunlin.wall_e.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.renchunlin.wall_e.R;
import com.example.renchunlin.wall_e.adapter.EveAdapter;
import com.example.renchunlin.wall_e.entity.EveData;
import com.example.renchunlin.wall_e.utils.PicassoUtils;
import com.example.renchunlin.wall_e.utils.StaticClass;
import com.example.renchunlin.wall_e.view.CustomDialog;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

/*
 *  项目名:     WALL_E
 *  包名:       com.example.rcl.smartbutler.fragment
 *  文件名:     FraEve
 *  创建者:     RCL
 *  创建时间:   2016/9/29 11:46
 *  描述:       夏娃
 */
public class FraEve extends Fragment {

    private GridView mGridView;
    private EveData data;
    private List<EveData> mList=new ArrayList<>();
    //提示框
    private CustomDialog dialog;
    //预览图片
    private ImageView iv_img;
    //图片地址的数据
    private List<String> mListUrl=new ArrayList<>();
    //photoView
    private PhotoViewAttacher mAttacher;
    /*
    * 1.监听点击事件
    * 2.提示框
    * 3.加载图片
    * 4.photoView
    * */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_eve,null);
        findView(view);
        return view;
    }

    private void findView(View view) {
        mGridView= (GridView) view.findViewById(R.id.mGridView);

        //初始化提示框
        dialog=new CustomDialog(getActivity(), LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT,R.layout.dialog_eve,R.style.Theme_dialog, Gravity.CENTER);
        iv_img= (ImageView) dialog.findViewById(R.id.iv_img);

        //解析
        RxVolley.get(StaticClass.GIRL_URL, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                //Toast.makeText(getActivity(),t, Toast.LENGTH_SHORT).show();
                parsingJson(t);
            }
        });

        //监听点击事件
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //解析图片
                PicassoUtils.loadImageView(getActivity(),mListUrl.get(position),iv_img);
                //缩放
                mAttacher=new PhotoViewAttacher(iv_img);
                //刷新
                mAttacher.update();
                dialog.show();
            }
        });
    }

    private void parsingJson(String t) {
        try {
            JSONObject jsonObject=new JSONObject(t);
            JSONArray jsonArray=jsonObject.getJSONArray("results");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject json= (JSONObject) jsonArray.get(i);
                data=new EveData();

                String url=json.getString("url");
                data.setImgUrl(url);
                mList.add(data);
                mListUrl.add(url);
            }
            EveAdapter adapter=new EveAdapter(getActivity(),mList);
            mGridView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
