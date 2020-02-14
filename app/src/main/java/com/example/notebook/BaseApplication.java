package com.example.notebook;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.notebook.Util.CommonUtil;
import com.example.notebook.Util.TransformationScale;
import com.example.notebook.db.GroupDao;
import com.example.notebook.db.TaskDao;
import com.example.notebook.db.UserDao;
import com.sendtion.xrichtext.IImageLoader;
import com.sendtion.xrichtext.XRichText;

import java.util.Date;


public class BaseApplication extends Application {
    private static final String TAG = "BaseApplication";
    private static BaseApplication baseApplication;
    private UserDao userDao;
    private GroupDao groupDao;
    private TaskDao taskDao;

    private String user_name;
    private String user_pwd;

    public static Context getInstance() {
        if (baseApplication == null) {
            baseApplication = new BaseApplication();
        }
        return baseApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        XRichText.getInstance().setImageLoader(new IImageLoader() {
            @Override
            public void loadImage(final String imagePath, final ImageView imageView, final int imageHeight) {
                Log.e("---", "imageHeight: " + imageHeight);
                //如果是网络图片
                if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
                    Glide.with(getApplicationContext()).asBitmap().load(imagePath).dontAnimate()
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                    if (imageHeight > 0) {//固定高度
                                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                                                FrameLayout.LayoutParams.MATCH_PARENT, imageHeight);//固定图片高度，记得设置裁剪剧中
                                        lp.bottomMargin = 10;//图片的底边距
                                        imageView.setLayoutParams(lp);
                                        Glide.with(getApplicationContext()).asBitmap().load(imagePath).centerCrop()
                                                .placeholder(R.mipmap.img_load_fail).error(R.mipmap.img_load_fail).into(imageView);
                                    } else {//自适应高度
                                        Glide.with(getApplicationContext()).asBitmap().load(imagePath)
                                                .placeholder(R.mipmap.img_load_fail).error(R.mipmap.img_load_fail).into(new TransformationScale(imageView));
                                    }
                                }
                            });
                } else { //如果是本地图片
                    if (imageHeight > 0) {//固定高度
                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT, imageHeight);//固定图片高度，记得设置裁剪剧中
                        lp.bottomMargin = 10;//图片的底边距
                        imageView.setLayoutParams(lp);

                        Glide.with(getApplicationContext()).asBitmap().load(imagePath).centerCrop()
                                .placeholder(R.mipmap.img_load_fail).error(R.mipmap.img_load_fail).into(imageView);
                    } else {//自适应高度
                        Glide.with(getApplicationContext()).asBitmap().load(imagePath)
                                .placeholder(R.mipmap.img_load_fail).error(R.mipmap.img_load_fail).into(new TransformationScale(imageView));
                    }
                }
            }
        });
    }


    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_pwd() {
        return user_pwd;
    }

    public void setUser_pwd(String user_pwd) {
        this.user_pwd = user_pwd;
    }

}

