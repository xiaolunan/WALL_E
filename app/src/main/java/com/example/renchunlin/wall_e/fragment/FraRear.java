package com.example.renchunlin.wall_e.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.renchunlin.wall_e.R;
import com.example.renchunlin.wall_e.entity.MyUser;
import com.example.renchunlin.wall_e.ui.CourierActivity;
import com.example.renchunlin.wall_e.ui.LoginActivity;
import com.example.renchunlin.wall_e.ui.PhoneActivity;
import com.example.renchunlin.wall_e.utils.UtilTools;
import com.example.renchunlin.wall_e.view.CustomDialog;
import java.io.File;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

/*
 *  项目名:     WALL_E
 *  包名:       com.example.rcl.smartbutler.fragment
 *  文件名:     FraRear
 *  创建者:     RCL
 *  创建时间:   2016/9/29 10:02
 *  描述:       后
 */
public class FraRear extends Fragment implements View.OnClickListener{

    private Button btn_exit_user,btn_update_ok;
    private TextView edit_user;
    private EditText et_username,et_sex,et_age,et_desc;

    //圆形头像
    private CircleImageView profile_image;

    private CustomDialog dialog;

    private Button btn_camera,btn_picture,btn_cancel;

    //快递查询
    private TextView tv_courier,tv_phone;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_personal,null);
        findView(view);
        return view;
    }
    //初始化V
    private void findView(View view) {
        tv_phone= (TextView) view.findViewById(R.id.tv_phone);
        tv_phone.setOnClickListener(this);
        tv_courier= (TextView) view.findViewById(R.id.tv_courier);
        tv_courier.setOnClickListener(this);
        btn_exit_user= (Button) view.findViewById(R.id.btn_exit_user);
        btn_exit_user.setOnClickListener(this);
        edit_user= (TextView) view.findViewById(R.id.edit_user);
        edit_user.setOnClickListener(this);

        et_username= (EditText) view.findViewById(R.id.et_username);
        et_sex= (EditText) view.findViewById(R.id.et_sex);
        et_age= (EditText) view.findViewById(R.id.et_age);
        et_desc= (EditText) view.findViewById(R.id.et_desc);
        //默认是不可以点击|不可以输入
        setEnabled(false);

        //设置具体的值
        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        et_username.setText(userInfo.getUsername());
        et_age.setText(userInfo.getAge()+"");
        et_desc.setText(userInfo.getDesc());
        et_sex.setText(userInfo.isSex()?"男":"女");

        btn_update_ok= (Button) view.findViewById(R.id.btn_update_ok);
        btn_update_ok.setOnClickListener(this);
        profile_image= (CircleImageView) view.findViewById(R.id.profile_image);
        profile_image.setOnClickListener(this);

        UtilTools.getImageToShare(getActivity(),profile_image);

        //初始化dialog
        dialog=new CustomDialog(getActivity(),0,0,R.layout.dialog_photo, R.style.pop_anim_style, Gravity.BOTTOM,0);
        //屏幕外点击无效
        dialog.setCancelable(false);

        btn_camera= (Button) dialog.findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(this);
        btn_picture= (Button) dialog.findViewById(R.id.btn_picture);
        btn_picture.setOnClickListener(this);
        btn_cancel= (Button) dialog.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);
    }

    private void setEnabled(boolean is){
        et_username.setEnabled(is);
        et_sex.setEnabled(is);
        et_age.setEnabled(is);
        et_desc.setEnabled(is);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_exit_user:
                //退出登录
                //清除缓存用户对象
                MyUser.logOut();
                // 现在的currentUser是null了
                BmobUser currentUser = MyUser.getCurrentUser();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
            //编辑资料
            case R.id.edit_user:
                btn_update_ok.setVisibility(View.VISIBLE);
                setEnabled(true);
                break;
            case R.id.btn_update_ok:
                //1.拿到输入框的值
                String username=et_username.getText().toString().trim();
                String sex=et_sex.getText().toString().trim();
                String age=et_age.getText().toString().trim();
                String desc=et_desc.getText().toString().trim();

                //2.判断是否为空
                if(!TextUtils.isEmpty(username)&&!TextUtils.isEmpty(sex)&&!TextUtils.isEmpty(age)){
                    //3.更新属性
                    MyUser user = new MyUser();
                    user.setUsername(username);
                    user.setAge(Integer.parseInt(age));
                    if(sex.equals(getString(R.string.text_boy))){
                        user.setSex(true);
                    }else {
                        user.setSex(false);
                    }
                    //简介
                    if(!TextUtils.isEmpty(desc)){
                        user.setDesc(desc);
                    }else {
                        user.setDesc(getString(R.string.text_nothing));
                    }

                    BmobUser bmobUser= BmobUser.getCurrentUser();
                    user.update(bmobUser.getObjectId(),new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                setEnabled(false);
                                btn_update_ok.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "更新用户信息成功", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(), "更新用户信息失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else {
                    Toast.makeText(getActivity(),getString(R.string.text_tost_empty), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.profile_image:
                dialog.show();
                break;

            case R.id.btn_cancel:
                dialog.dismiss();
                break;
            case R.id.btn_camera:
                toCamera();
                break;
            case R.id.btn_picture:
                toPicture();
                break;
            case R.id.tv_courier:
                startActivity(new Intent(getActivity(),CourierActivity.class));
                break;
            case R.id.tv_phone:
                startActivity(new Intent(getActivity(),PhoneActivity.class));
                break;
        }
    }

    public static final String PHOTO_IMAGE_FILE_NAME="fileImg.jpg";
    public static final int CAMERA_REQUEST_CODE=100;
    public static final int IMAGE_REQUEST_CODE=101;
    public static final int RESULT_REQUEST_CODE=102;
    private File tempFile=null;

    //跳转相册
    private void toPicture() {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_REQUEST_CODE);
        dialog.dismiss();
    }

    //跳转相机
    private void toCamera() {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断内存卡是否可用，可用的话就进行存储
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),PHOTO_IMAGE_FILE_NAME)));
        startActivityForResult(intent,CAMERA_REQUEST_CODE);
        dialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!=getActivity().RESULT_CANCELED){
            switch(requestCode){
                //相册数据
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                //相机数据
                case CAMERA_REQUEST_CODE:
                    tempFile=new File(Environment.getExternalStorageDirectory(),PHOTO_IMAGE_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(tempFile));
                    break;
                case RESULT_REQUEST_CODE:
                    //有可能会点击舍弃
                    if(data!=null){
                        //拿到图片设置
                        setImageToView(data);
                        //既然已经设置了图片，我们原先的就应该删除
                        if(tempFile!=null){
                            tempFile.delete();
                        }
                    }
                    break;
            }
        }
    }
    //裁剪
    private void startPhotoZoom(Uri uri){
        if(uri==null){
            Log.i("TAG", "uri==null");
            return;
        }
        Intent intent=new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri,"image/*");
        //设置裁剪
        intent.putExtra("crop","true");
        //裁剪宽高比例
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        //裁剪图片的质量
        intent.putExtra("outputX",320);
        intent.putExtra("outputY",320);
        //发送数据
        intent.putExtra("return-data",true);
        startActivityForResult(intent,RESULT_REQUEST_CODE);
    }
    //设置图片
    private void setImageToView(Intent data){
        Bundle bundle=data.getExtras();
        if(bundle!=null){
            Bitmap bitmap=bundle.getParcelable("data");
            profile_image.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //保存
        UtilTools.putImageToShare(getActivity(),profile_image);
    }
}
