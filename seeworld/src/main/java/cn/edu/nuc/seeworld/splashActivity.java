package cn.edu.nuc.seeworld;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.bmob.sms.BmobSMS;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.edu.nuc.seeworld.entity.MyUser;
import cn.edu.nuc.seeworld.utils.DisplayUtil;
import cn.edu.nuc.seeworld.utils.WindowsHandWUtiles;

/**
 * Created by lenovo on 2015/9/19.
 */
public class splashActivity extends Activity {

    private long SPLASH_MILLIS = 2000;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, Config.BOMB_KEY);
        BmobSMS.initialize(this, Config.BOMB_KEY);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.splash, null, false);
        addContentView(relativeLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
         Config.ScreenH=WindowsHandWUtiles.getWindowsHandW(this).H;
        Config.ScreenW=WindowsHandWUtiles.getWindowsHandW(this).W;
        textView= (TextView) findViewById(R.id.tv_hello);
        textView.setAlpha(0.15f);
        textView.setY(Config.ScreenH- DisplayUtil.dip2px(this,80));
        ObjectAnimator alphaanimator = ObjectAnimator.ofFloat(textView, "alpha", 1.0f);
        ObjectAnimator translationYanimator = ObjectAnimator.ofFloat(textView, "translationY",Config.ScreenH- DisplayUtil.dip2px(this,121));
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(alphaanimator, translationYanimator);
        animatorSet.setDuration(700);
//        textView.animate().alpha(1.0f).y(559.0f).y(700.0f).setDuration(1000);
        ObjectAnimator translationYanimator2 = ObjectAnimator.ofFloat(textView, "translationY", Config.ScreenH- DisplayUtil.dip2px(this,116));
        translationYanimator2.setDuration(150);
        AnimatorSet animatorSet1=new AnimatorSet();
        animatorSet1.playSequentially(animatorSet,translationYanimator2);
        animatorSet1.start();
        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MyUser userInfo = BmobUser.getCurrentUser(splashActivity.this, MyUser.class);
                Intent intent=null;
                if(userInfo!=null){
                    intent=new Intent(splashActivity.this,MainActivity.class);
                }else {
                    intent=new Intent(splashActivity.this,LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        },SPLASH_MILLIS);

    }
}
