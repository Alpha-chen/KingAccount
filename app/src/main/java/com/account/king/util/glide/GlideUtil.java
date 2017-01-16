package com.account.king.util.glide;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.account.king.R;
import com.bumptech.glide.Glide;


/**
 * @author king
 */
public class GlideUtil {


    public static void load(Context context, String path, ImageView imageView) {
        Glide.with(context).load(path).into(imageView);
    }

    public static void load(Activity context, String path, ImageView imageView) {
        Glide.with(context).load(path).into(imageView);
    }

    public static void load(FragmentActivity context, String path, ImageView imageView) {
        Glide.with(context).load(path).into(imageView);
    }

    public static void load(Fragment context, String path, ImageView imageView) {
        Glide.with(context).load(path).into(imageView);
    }

    public static void load(android.app.Fragment context, String path, ImageView imageView) {
        Glide.with(context).load(path).into(imageView);
    }

    /*----------------圆角--------------*/
    public static void loadRound(Context context, String path, ImageView imageView) {
        Glide.with(context).load(path).transform(new GlideRoundTransform(context)).centerCrop()
                .placeholder(R.drawable.photo_example).into(imageView);
    }

    /*------------居中放大-----------------*/

    public static void loadFitCenter(Activity context, String path, ImageView imageView) {
        Glide.with(context).load(path).fitCenter().into(imageView);
    }

    public static void loadCenterCrop(Activity context, String path, ImageView imageView) {
        Glide.with(context).load(path).centerCrop().into(imageView);
    }
    /*-------------查看相册 asBitmap----------------*/

    public static void loadPhoto(Activity context, String path, ImageView imageView) {
        Glide.with(context).load(path).asBitmap().placeholder(R.drawable.photo_example).into(imageView);
    }

    public static void loadPhoto(Context context, String path, ImageView imageView) {
        Glide.with(context).load(path).asBitmap().placeholder(R.drawable.photo_example).into(imageView);
    }

}
