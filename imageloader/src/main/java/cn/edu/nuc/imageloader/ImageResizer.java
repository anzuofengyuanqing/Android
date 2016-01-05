package cn.edu.nuc.imageloader;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.FileDescriptor;

/**
 * Created by lenovo on 2016/1/5.
 */
public class ImageResizer {
    private static final String TAG="ImageResizer";
    public Bitmap decodeSampledBitmapFromResource(Resources res,int resId,int reqWidth,int reqHeight){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeResource(res,resId,options);
        options.inSampleSize=calculateInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeResource(res,resId,options);
    }

    public Bitmap decodeSampledBitmapFromFileDescriptor(FileDescriptor fd,int reqWidth,int reqHeight){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFileDescriptor(fd, null, options);
        options.inSampleSize=calculateInSampleSize(options,reqWidth,reqHeight);
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeFileDescriptor(fd,null,options);
    }
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
            if(reqWidth==0||reqHeight==0){
                return 1;
            }
            final int height=options.outHeight;
            final int width=options.outWidth;
            Log.d(TAG,"orgin,w="+width+" h="+height);
            int inSampleSize=1;
            if(height>=reqHeight||width>=reqHeight){
                final int halfWidth=width/2;
                final int halfHeight=height/2;
                while ((halfHeight/inSampleSize)>=reqHeight&&(halfWidth/inSampleSize)>=reqWidth){
                    inSampleSize*=2;
                }
            }
        Log.d(TAG,"sampleSize:"+inSampleSize);
        return inSampleSize;
    }
}
