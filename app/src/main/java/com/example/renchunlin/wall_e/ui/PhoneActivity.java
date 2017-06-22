package com.example.renchunlin.wall_e.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.renchunlin.wall_e.R;
import com.example.renchunlin.wall_e.utils.L;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目名：   WALL_E
 * 包名：     com.example.rcl.smartbutler.ui
 * 文件名：   PhoneActivity
 * 创建者：   RCL
 * 创建时间： 2016/9/5 10:02
 * 描述：     归属地查询
 */
public class PhoneActivity extends AppCompatActivity implements View.OnClickListener {

    //输入框
    private EditText et_number;
    //公司Logo
    private ImageView iv_company;
    //结果
    private TextView tv_result;

    private Button btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9, btn_del, btn_query;

    //标记位
    private boolean flag=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        initView();
    }

    private void initView() {
        et_number = (EditText) findViewById(R.id.et_number);
        iv_company = (ImageView) findViewById(R.id.iv_company);
        tv_result = (TextView) findViewById(R.id.tv_result);

        btn_0 = (Button) findViewById(R.id.btn_0);
        btn_0.setOnClickListener(this);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_1.setOnClickListener(this);
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_2.setOnClickListener(this);
        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_3.setOnClickListener(this);
        btn_4 = (Button) findViewById(R.id.btn_4);
        btn_4.setOnClickListener(this);
        btn_5 = (Button) findViewById(R.id.btn_5);
        btn_5.setOnClickListener(this);
        btn_6 = (Button) findViewById(R.id.btn_6);
        btn_6.setOnClickListener(this);
        btn_7 = (Button) findViewById(R.id.btn_7);
        btn_7.setOnClickListener(this);
        btn_8 = (Button) findViewById(R.id.btn_8);
        btn_8.setOnClickListener(this);
        btn_9 = (Button) findViewById(R.id.btn_9);
        btn_9.setOnClickListener(this);
        btn_del = (Button) findViewById(R.id.btn_del);
        btn_del.setOnClickListener(this);
        btn_query = (Button) findViewById(R.id.btn_query);
        btn_query.setOnClickListener(this);

        //长按事件
        btn_del.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                et_number.setText("");
                return false;
            }
        });
    }

    //点击事件
    @Override
    public void onClick(View v) {
        /*
        *  逻辑
        *  1.获取输入框的内容
        *  2.判断是否为空
        *  3.网络请求
        *  4.解析Json
        *  键盘逻辑
        * */
        String str=et_number.getText().toString().trim();
        switch (v.getId()){
            case R.id.btn_0:
            case R.id.btn_1:
            case R.id.btn_2:
            case R.id.btn_3:
            case R.id.btn_4:
            case R.id.btn_5:
            case R.id.btn_6:
            case R.id.btn_7:
            case R.id.btn_8:
            case R.id.btn_9:
                if(flag){
                    flag=false;
                    str="";
                    et_number.setTag(str);
                }
                //每次结尾添加1
                et_number.setText(str+((Button)v).getText());
                //移动光标
                et_number.setSelection(str.length()+1);
                break;
            case R.id.btn_del:
                if(!TextUtils.isEmpty(str)&&str.length()>0){
                    //每次结尾减去1
                    et_number.setText(str.substring(0,str.length()-1));
                    //移动光标
                    et_number.setSelection(str.length()-1);
                }
                break;
            case R.id.btn_query:
                if(!TextUtils.isEmpty(str)){
                    getPhone(str);
                }
                break;
        }
    }

    private void getPhone(String str) {
        String url="https://www.iteblog.com/api/mobile.php?mobile="+str+"";

        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                //Toast.makeText(PhoneActivity.this,t,Toast.LENGTH_SHORT).show();
                L.i("json:"+t);
                parsingJson(t);
            }
        });
    }

    /*
    * "ID":"269130",
    "prefix":"1838393",
    "province":"四川",
    "city":"广元",
    "operator":"中国移动",
    "areaCode":"0839",
    "zip":"628000",
    "ret":0,
    "searchStr":"18383934095",
    * */

    private void parsingJson(String t) {
        try {
            JSONObject jsonObject=new JSONObject(t);
            String province=jsonObject.getString("province");
            String city=jsonObject.getString("city");
            String operator=jsonObject.getString("operator");
            String areaCode=jsonObject.getString("areaCode");
            String zip=jsonObject.getString("zip");
            String searchStr=jsonObject.getString("searchStr");

            tv_result.setText(getString(R.string.text_attribution)+province+city+"\n"
                    +getString(R.string.text_area_code)+areaCode+"\n"
                    +getString(R.string.text_zip_code)+zip+"\n"
                    +getString(R.string.text_operator)+operator+"\n"
                    +getString(R.string.text_cell_phone_number)+searchStr+"\n");

            //图片显示
            switch (operator){
                case "中国移动":
                    iv_company.setBackgroundResource(R.drawable.china_mobile);
                    break;
                case "中国联通":
                    iv_company.setBackgroundResource(R.drawable.china_unicom);
                    break;
                case "中国电信":
                    iv_company.setBackgroundResource(R.drawable.china_telecom);
                    break;
            }

            flag=true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
