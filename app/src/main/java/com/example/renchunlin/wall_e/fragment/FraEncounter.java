package com.example.renchunlin.wall_e.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.renchunlin.wall_e.R;
import com.example.renchunlin.wall_e.adapter.EncounterAdapter;
import com.example.renchunlin.wall_e.entity.EncounterData;
import com.example.renchunlin.wall_e.ui.WebViewActivity;
import com.example.renchunlin.wall_e.utils.StaticClass;
import com.example.renchunlin.wall_e.view.LoadListView;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
 *  项目名:     WALL_E
 *  包名:       com.example.rcl.smartbutler.fragment
 *  文件名:     FraEncounter
 *  创建者:     RCL
 *  创建时间:   2016/8/29 12:44
 *  描述:       瓦力
 */
public class FraEncounter extends Fragment implements LoadListView.ILoadListener{

    private LoadListView mListView;
    private EncounterData data;
    private List<EncounterData> mList=new ArrayList<>();
    //标题
    private List<String> mListTitle=new ArrayList<>();
    //地址
    private List<String> mListUrl=new ArrayList<>();

    private EncounterAdapter adapter;

    int count=1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_selected,null);

        initData();
        findView(view,mList);
        return view;
    }

    public void findView(View view,List<EncounterData> mList) {
        mListView= (LoadListView) view.findViewById(R.id.mListView);
        getData();

        //点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(), WebViewActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("key","value");
                intent.putExtra("title",mListTitle.get(position));
                intent.putExtra("url",mListUrl.get(position));
                startActivity(intent);

            }
        });
    }

    public void getData(){
        if (adapter == null) {
            mListView.setInterface(this);
            adapter = new EncounterAdapter(getActivity(), mList);
            mListView.setAdapter(adapter);
        } else {
            adapter.onDateChange(mList);
            adapter.notifyDataSetChanged();
        }
    }

    private void initData() {
        String url="https://api.tianapi.com/wxnew/?key="+ StaticClass.WECHAT_KEY+"&num=10&page=0";
        //解析接口
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                //Toast.makeText(getActivity(),t, Toast.LENGTH_SHORT).show();
                parsingJson(t);
            }
        });
    }

    private void initLoadData() {
        count++;
        String url="https://api.tianapi.com/wxnew/?key="+ StaticClass.WECHAT_KEY+"&num=10&page="+count+"";
        //解析接口
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                //Toast.makeText(getActivity(),t, Toast.LENGTH_SHORT).show();
                parsingLoadJson(t);
            }
        });
    }

    private void parsingJson(String t) {
        try {
            JSONObject jsonObject=new JSONObject(t);
            JSONArray jsonArray=jsonObject.getJSONArray("newslist");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject json= (JSONObject) jsonArray.get(i);
                data=new EncounterData();

                String title=json.getString("title");
                String url=json.getString("url");

                data.setTitle(title);
                data.setDescription(json.getString("description"));
                data.setPicUrl(json.getString("picUrl"));
                mList.add(data);

                mListTitle.add(title);
                mListUrl.add(url);
            }
            //adapter=new EncounterAdapter(getActivity(),mList);
            //mListView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parsingLoadJson(String t) {
        try {
            JSONObject jsonObject=new JSONObject(t);
            JSONArray jsonArray=jsonObject.getJSONArray("newslist");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonLoad= (JSONObject) jsonArray.get(i);
                data=new EncounterData();

                String title=jsonLoad.getString("title");
                String url=jsonLoad.getString("url");

                data.setTitle(title);
                data.setDescription(jsonLoad.getString("description"));
                data.setPicUrl(jsonLoad.getString("picUrl"));
                mList.add(data);

                mListTitle.add(title);
                mListUrl.add(url);
            }
            //adapter=new EncounterAdapter(MainActivity.this,mList);
            //mListView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoad() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                //获取更多数据
                initLoadData();
                //更新listview显示；
                getData();
                //通知listview加载完毕
                mListView.loadComplete();
            }
        }, 2000);
    }
}
