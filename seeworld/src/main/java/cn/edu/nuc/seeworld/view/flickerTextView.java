package cn.edu.nuc.seeworld.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by lenovo on 2015/9/13.
 */
public class flickerTextView extends TextView {
    int mViewWidth;
    Paint mPaint;
    LinearGradient mLinerGradient;
    Matrix mGradientMatrix;
    int mTranslate;

    public flickerTextView(Context context) {
        super(context);
    }

    public flickerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mGradientMatrix!=null){
            mTranslate+=mViewWidth/5;
            if(mTranslate>2*mViewWidth){
                mTranslate=-mViewWidth;
            }
            mGradientMatrix.setTranslate(mTranslate,0);
            mLinerGradient.setLocalMatrix(mGradientMatrix);
            postInvalidateDelayed(100);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(mViewWidth==0){
            mViewWidth=getMeasuredWidth();
            if(mViewWidth>0){
                mPaint=getPaint();
                mLinerGradient=new LinearGradient(0,0,mViewWidth,0,
                        new int[]{Color.BLACK,0xffffffff,Color.RED,Color.YELLOW,Color.BLUE},null, Shader.TileMode.CLAMP);
                mPaint.setShader(mLinerGradient);
                mGradientMatrix=new Matrix();

            }
        }
    }
}
