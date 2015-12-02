package cn.edu.nuc.seeworld.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by lenovo on 2015/9/21.
 */
public class WindowsHandWUtiles {

    public static HandW handw;
    public static HandW getWindowsHandW(Activity activity){
        handw=new HandW();
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        handw.H=metric.heightPixels;
        handw.W=metric.widthPixels;
        return handw;

    }


    public static class HandW{
        public int H;
        public int W;
    }
}
