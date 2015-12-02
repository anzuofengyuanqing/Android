package cn.edu.nuc.seeworld;

import com.tencent.mapsdk.raster.model.LatLng;

import cn.edu.nuc.seeworld.entity.MyUser;

/**
 * Created by lenovo on 2015/9/10.
 */
public class Config {
    public static boolean isStreeFg=false;
    public static LatLng clickLatlng=new LatLng(39.984122, 116.307894);
    public static LatLng mylatlng=new LatLng(39.984122, 116.307894);
    public static boolean isclickmap=false;
    public static final String BOMB_KEY="36f35db8c2eb36442ed689d5c4c6b0ab";
    public static final String USER="user";
    public static MyUser CurrUser=null;
    public static int ScreenH=1280;
    public static int ScreenW=720;
    public static boolean videofinish=false;
    public static String  video_path=null;
}
