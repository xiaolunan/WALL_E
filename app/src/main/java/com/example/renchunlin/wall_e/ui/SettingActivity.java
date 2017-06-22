package com.example.renchunlin.wall_e.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.example.renchunlin.wall_e.R;
import com.example.renchunlin.wall_e.utils.ShareUtils;
import com.example.renchunlin.wall_e.utils.StaticClass;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目名：   WALL_E
 * 包名：     com.example.renchunlin.login.ui
 * 文件名：   SettingActivity
 * 创建者：   RCL
 * 创建时间： 2016/9/2 16:37
 * 描述：     设置
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    //语音播报
    private Switch sw_speak;
    private LinearLayout ll_update;
    private TextView tv_version;

    private String versionName;
    private int versionCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }

    private void initView() {
        ll_update = (LinearLayout) findViewById(R.id.ll_update);
        ll_update.setOnClickListener(this);
        tv_version = (TextView) findViewById(R.id.tv_version);

        sw_speak = (Switch) findViewById(R.id.sw_speak);
        sw_speak.setOnClickListener(this);

        boolean isSpeak = ShareUtils.getBoolean(this, "isSpeak", false);
        sw_speak.setChecked(isSpeak);

        try {
            getVersionNameCode();
            tv_version.setText("检测版本"+versionName);
        } catch (PackageManager.NameNotFoundException e) {
            tv_version.setText("检测版本");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sw_speak:
                //切换相反
                sw_speak.setSelected(!sw_speak.isSelected());
                //保存状态
                ShareUtils.putBoolean(this, "isSpeak", sw_speak.isChecked());
                break;

            case R.id.ll_update:
                /*
                * 步骤
                * 1.请求服务器的配置文件，拿到code
                * 2.比较
                * 3.dialog提示
                * 4.跳转到更新界面，并且把url传递过去
                * */

                RxVolley.get(StaticClass.CHECK_UPDATE_URL, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        /*L.i("json"+t);
                        Toast.makeText(SettingActivity.this, t.toString(), Toast.LENGTH_SHORT).show();*/
                        parsingJson(t);
                    }
                });

                break;
        }
    }

    //解析json
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject=new JSONObject(t);
            int code=jsonObject.getInt("versionCode");
            if(code>versionCode){
                showUpdateDialog(jsonObject.getString("content"));
            }else{
                Toast.makeText(this, "当前是最新版本", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //弹出升级提示
    private void showUpdateDialog(String content) {
        new AlertDialog.Builder(this)
                .setTitle("有新版本了!")
                //content
                .setMessage("修复多项Bug!")
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(SettingActivity.this,UpdateActivity.class));
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //什么都不做，也会执行dismis方法
            }
        }).show();
    }

    //获取版本号/Code
    private void getVersionNameCode() throws PackageManager.NameNotFoundException {
        PackageManager pm = getPackageManager();
        PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
        versionName = info.versionName;
        versionCode = info.versionCode;
    }
}
