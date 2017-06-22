package com.example.renchunlin.wall_e.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;


/**
 * 项目名：   WALL_E
 * 包名：     com.example.rcl.smartbutler.utils
 * 文件名：   PicassoUtils
 * 创建者：   RCL
 * 创建时间： 2016/9/6 11:48
 * 描述：     Picasso封装
 */
public class PicassoUtils {
    //默认加载图片
    public static void loadImageView(Context mContext, String url, ImageView imageView) {
        Picasso.with(mContext).load(url).into(imageView);
    }

    //默认加载图片(指定大小)
    public static void loadImageViewSize(Context mContext, String url, int width, int height, ImageView imageView) {
        Picasso.with(mContext).load(url).resize(width, height).centerCrop().into(imageView);
    }

    //加载图片有默认图片
    public static void loadImageViewHolder(Context mContext, String url,int loadImg,int errorImg, ImageView imageView) {
        Picasso.with(mContext)
                .load(url)
                .placeholder(loadImg)
                .error(errorImg)
                .into(imageView);
    }
    //裁剪图片
    public static void loadImageViewCrop(Context mContext, String url,ImageView imageView){
        Picasso.with(mContext).load(url).transform(new CropSquareTransformation()).into(imageView);
    }

    //按比例裁剪 矩形
    public static class CropSquareTransformation implements Transformation {
        @Override public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                //回收
                source.recycle();
            }
            return result;
        }
        @Override public String key() {
            return "rcl";
        }
    }
}
