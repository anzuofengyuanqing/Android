package cn.edu.nuc.seeworld.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by lenovo on 2015/10/12.
 */
public class BitmapUtils {
    public static Bitmap compressImage(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        int options=100;
        while (baos.toByteArray().length/1024>150){
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options-=10;
        }
        ByteArrayInputStream isBm=new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap1= BitmapFactory.decodeStream(isBm,null,null);
        return bitmap1;
    }
    public static Bitmap resizePhoto(Bitmap bitmap){
        float width=bitmap.getWidth();
        float height=bitmap.getHeight();
        Matrix matrix=new Matrix();
        float scaleWidth=1600.0f/width;
        float scaleHeidth=900.0f/height;
        matrix.postScale(scaleWidth,scaleHeidth);
        Bitmap bitmap1=Bitmap.createBitmap(bitmap,0,0,(int)width,(int)height,matrix,true);
        return bitmap1;
    }

}
