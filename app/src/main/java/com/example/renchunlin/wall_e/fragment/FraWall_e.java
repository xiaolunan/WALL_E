package com.example.renchunlin.wall_e.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.example.renchunlin.wall_e.R;
import com.example.renchunlin.wall_e.adapter.ChatListAdapter;
import com.example.renchunlin.wall_e.entity.ChatListData;
import com.example.renchunlin.wall_e.utils.ShareUtils;
import com.example.renchunlin.wall_e.utils.StaticClass;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/*
 *  项目名:     WALL_E
 *  包名:       com.example.renchunlin.wall_e.ui;
 *  文件名:     FragemntButler
 *  创建者:     RCL
 *  创建时间:   2016/9/29 12:44
 *  描述:       瓦力
 */
public class FraWall_e extends Fragment implements View.OnClickListener {

    private ListView mChatListView;
    private List<ChatListData> mList=new ArrayList<>();
    ChatListAdapter adapter;

    //输入框
    private EditText et_text;
    //发送按钮
    private Button btn_send;

    //TTS
    private SpeechSynthesizer mTts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_butler,null);
        findView(view);
        return view;
    }

    //初始化View
    private void findView(View view) {

        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        mTts = SpeechSynthesizer.createSynthesizer(getActivity(), null);
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "aisbabyxu");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.PITCH, "50");//设置音调
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
        //保存在SD卡需要在AndroidManifest.xml添加写SD卡权限
        //如果不需要保存合成音频，注释该行代码
        //mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");


        et_text= (EditText) view.findViewById(R.id.et_text);
        btn_send= (Button) view.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);

        mChatListView= (ListView) view.findViewById(R.id.mChatListView);

        //设置适配器
        adapter=new ChatListAdapter(getActivity(),mList);
        mChatListView.setAdapter(adapter);

        addLeftItem(getString(R.string.text_Eve));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_send:
                /*
                * 逻辑
                * 1.获取输入框的内容
                * 2.判断是否为空
                * 3.发送给机器人请求返回内容
                * 4.清空当前的输入框
                * 5.添加你输入的内容到right item
                * 6.拿到机器人的返回值之后添加在left item
                * */
                String text=et_text.getText().toString().trim();
                if(!TextUtils.isEmpty(text)){
                    et_text.setText("");
                    addRightItem(text);
                    String url="http://op.juhe.cn/robot/index?info="+text+"&key="+ StaticClass.CHAT_LIST_KEY+"";
                    RxVolley.get(url, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            //Toast.makeText(getActivity(), t, Toast.LENGTH_SHORT).show();
                            //L.i("JSON:"+t);
                            parsingJson(t);
                        }
                    });
                }else{
                    Toast.makeText(getActivity(),getString(R.string.text_tost_empty), Toast.LENGTH_SHORT).show();
                    }
                break;
        }
    }

    /*{
        "reason":"成功的返回",
            "result":{
        "code":100000,
                "text":"你也好呀～有什么新鲜事儿？"
    },
        "error_code":0
    }*/
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject=new JSONObject(t);
            JSONObject jsonResult=jsonObject.getJSONObject("result");
            String text=jsonResult.getString("text");
            addLeftItem(text);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //添加左边文本
    private void addLeftItem(String text){
        boolean isSpeak= ShareUtils.getBoolean(getActivity(),"isSpeak",false);
        if(isSpeak){
            startSpeak(text);
        }
        ChatListData data=new ChatListData();
        data.setType(ChatListAdapter.VALUE_LEFT_TEXT);
        data.setText(text);
        mList.add(data);
        //通知adapter刷新
        adapter.notifyDataSetChanged();
        //滚动到底部
        mChatListView.setSelection(mChatListView.getBottom());
    }

    //添加右边文本
    private void addRightItem(String text){
        ChatListData data=new ChatListData();
        data.setType(ChatListAdapter.VALUE_RIGHT_TEXT);
        data.setText(text);
        mList.add(data);
        //通知adapter刷新
        adapter.notifyDataSetChanged();
        //滚动到底部
        mChatListView.setSelection(mChatListView.getBottom());
    }

    //添加右边文字
    private void startSpeak(String text){
        //3.开始合成
        mTts.startSpeaking(text, mSynListener);
    }

    //合成监听器
    private SynthesizerListener mSynListener = new SynthesizerListener() {
        //会话结束回调接口，没有错误时，error为null
        public void onCompleted(SpeechError error) {
        }

        //缓冲进度回调
        //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
        }

        //开始播放
        public void onSpeakBegin() {
        }

        //暂停播放
        public void onSpeakPaused() {
        }

        //播放进度回调
        //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        //恢复播放回调接口
        public void onSpeakResumed() {
        }

        //会话事件回调接口
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
        }
    };
}
