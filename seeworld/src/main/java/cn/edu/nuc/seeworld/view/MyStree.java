package cn.edu.nuc.seeworld.view;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tencent.tencentmap.streetviewsdk.StreetViewPanorama;
import com.tencent.tencentmap.streetviewsdk.StreetViewPanoramaView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cn.edu.nuc.seeworld.Config;
import cn.edu.nuc.seeworld.entity.MyMessage;

/**
 * Created by lenovo on 2015/9/11.
 */
public class MyStree extends StreetViewPanoramaView{
    private StreetViewPanorama streetViewPanorama=this.getStreetViewPanorama();
    Random random=new Random();
    Handler handler;
    AnimatorSet animatorSet=new AnimatorSet();
    List<Animator> animatorList=new ArrayList<>();
    List<TextView> tvList=new ArrayList<>();
//    private Boolean Flaggo=false;
//    private float x,y;
    public MyStree(Context context) {
        super(context);
    }
    public MyStree(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ViewGroup parent= (ViewGroup) this.getParent();
        if(parent!=null){
            requestDisallowInterceptTouchEvent(true);
        }
//        float xdown=-10000;
//        float ydown=-10000;
//        if(event.getAction()==MotionEvent.ACTION_DOWN);
//        {
//            Flaggo=true;
//            xdown=event.getX();
//            ydown=event.getY();
//        }
//        if(event.getAction()==MotionEvent.ACTION_UP&&Flaggo==true){
//            x=event.getX();
//            y=event.getY();
//            Flaggo=false;
//            if(x==xdown&&y==ydown){
//                Log.i("CLICK", "点击街景");
//
//            }
//        }
        return super.onTouchEvent(event);
    }
        public void publish(String content,int i){
            TextView textView=new TextView(this.getContext());
            textView.setText(content);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            int r=random.nextInt(3)+1;
            textView.setY(Config.ScreenH * 0.1f * r);
            textView.setX(-Config.ScreenW * (0.2f+i*0.4f));
            textView.setTextColor(Color.WHITE);
            textView.setAlpha(0.85f);
            textView.setTextSize(20);
            this.addView(textView);

            ObjectAnimator translationXanimator = ObjectAnimator.ofFloat(textView, "translationX",Config.ScreenW*1.2f);
            translationXanimator.setDuration(6000+1750*i);
            translationXanimator.start();

        }
    public void publishnow(String content){
        final TextView textView=new TextView(this.getContext());
        textView.setText(content);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int r=random.nextInt(3)+1;
        textView.setY(Config.ScreenH * 0.1f * r);
        textView.setX(-Config.ScreenW * 0.2f);
        textView.setTextColor(Color.WHITE);
        textView.setAlpha(0.85f);
        textView.setTextSize(20);
        this.addView(textView);
        ObjectAnimator translationXanimator = ObjectAnimator.ofFloat(textView, "translationX",Config.ScreenW*1.5f);
        translationXanimator.setDuration(6000);
        translationXanimator.start();
        translationXanimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                removeView(textView);
            }
        });

    }
    public void putcontent(final List<MyMessage> contentlist){
            for( int i=0;i<contentlist.size();i++){
                    publish(contentlist.get(i).getContenttext(),i);

            }
//        for(TextView textView:tvList){
//            AddView(textView);
//        }
//        invalidate();
    }
//    public void AddView(View view){
//        this.addView(view);
//    }


}
