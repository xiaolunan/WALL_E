package com.example.renchunlin.wall_e.application;

/*
 *  项目名：  WALL_E
 *  包名：    com.example.renchunlin.wall_e.ui;
 *  文件名:   BaseApplication
 *  创建者:   RCL
 *  创建时间:  2016/8/01 23:15
 *  描述：    Application
 */

import android.app.Application;

import com.example.renchunlin.wall_e.utils.StaticClass;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.tencent.bugly.crashreport.CrashReport;

import cn.bmob.v3.Bmob;

public class BaseApplication extends Application {

    //创建
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化Bugly
        CrashReport.initCrashReport(getApplicationContext(), StaticClass.BUGLY_APP_ID, true);
        //初始化Bmob
        Bmob.initialize(this, StaticClass.BMOB_APP_ID);
        // 将“12345678”替换成您申请的APPID，申请地址：http://open.voicecloud.cn
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID + "=" + StaticClass.VOICE_KEY);
    }
}
