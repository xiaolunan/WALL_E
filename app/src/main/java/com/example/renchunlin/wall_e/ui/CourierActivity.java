package com.example.renchunlin.wall_e.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.example.renchunlin.wall_e.R;
import com.example.renchunlin.wall_e.adapter.CourierAdapter;
import com.example.renchunlin.wall_e.entity.CourierData;
import com.example.renchunlin.wall_e.utils.L;
import com.example.renchunlin.wall_e.utils.StaticClass;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/*
 *  项目名:     WALL_E
 *  包名:       com.example.rcl.smartbutler.ui
 *  文件名:     CourierActivity
 *  创建者:     RCL
 *  创建时间:   2016/9/4 16:09
 *  描述:       快递查询
 */
public class CourierActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_name,et_number;
    private Button btn_get_courier;
    private ListView mListView;
    private List<CourierData> mList=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);

        initView();
    }

    private void initView() {
        et_name= (EditText) findViewById(R.id.et_name);
        et_number= (EditText) findViewById(R.id.et_number);
        btn_get_courier= (Button) findViewById(R.id.btn_get_courier);
        btn_get_courier.setOnClickListener(this);
        mListView= (ListView) findViewById(R.id.mListView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_get_courier:

                String name=et_name.getText().toString().trim();
                String number=et_number.getText().toString().trim();

                String url="http://www.zgw8.com/api.php/Express/index/key/"+ StaticClass.COURIER_KEY+"/language/json/kuaidigongsibianma/"+name+"/yundanhao/"+number+"";

                if(!TextUtils.isEmpty(name)&&!TextUtils.isEmpty(number)){

                    RxVolley.get(url, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            //Toast.makeText(CourierActivity.this,t,Toast.LENGTH_SHORT).show();
                            L.i("Json"+t);
                            parsingJson(t);
                        }
                    });

                }else{
                    Toast.makeText(this,getString(R.string.text_tost_empty),Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    //解析数据
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject=new JSONObject(t);
            JSONArray jsonArray=jsonObject.getJSONArray("body");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject json= (JSONObject) jsonArray.get(i);
                CourierData data=new CourierData();
                data.setContent(json.getString("content"));
                data.setTime(json.getString("time"));
                mList.add(data);
            }
            //倒序
            //Collections.reverse(mList);
            CourierAdapter adapter=new CourierAdapter(this,mList);
            mListView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
