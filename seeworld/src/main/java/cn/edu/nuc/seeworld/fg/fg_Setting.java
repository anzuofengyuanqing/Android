package cn.edu.nuc.seeworld.fg;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dd.CircularProgressButton;

import cn.bmob.v3.BmobUser;
import cn.edu.nuc.seeworld.LoginActivity;
import cn.edu.nuc.seeworld.R;
import cn.edu.nuc.seeworld.friendsActivity;

/**
 * Created by lenovo on 2015/9/9.
 */
public class fg_Setting extends Fragment {
    CircularProgressButton circularButton1;
    ImageView friendsbtn;
    Activity mActivity;
    ImageView recommendbtn;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.setting,container,false);
        mActivity=this.getActivity();
        circularButton1= (CircularProgressButton)view. findViewById(R.id.circularButton1);
        circularButton1.setIndeterminateProgressMode(true);
        circularButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toLogin();
            }
        });
        friendsbtn= (ImageView) view.findViewById(R.id.btn_friends);
        recommendbtn= (ImageView) view.findViewById(R.id.btn_recommend);
        friendsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotofriends();
            }
        });
        recommendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(mActivity);
                LayoutInflater inflater = LayoutInflater.from(mActivity);
                View view = inflater.inflate(R.layout.recommend, null);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return view;
    }
    private void toLogin(){
        circularButton1.setProgress(50);
        BmobUser.logOut(mActivity);   //清除缓存用户对象
        BmobUser currentUser = BmobUser.getCurrentUser(mActivity); // 现在的currentUser是null了
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                circularButton1.setProgress(100);
            }
        }, 500);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(mActivity, LoginActivity.class);
                startActivity(intent);
                mActivity.finish();
            }
        }, 1000);
    }
    private void gotofriends(){
        Intent intent=new Intent(mActivity, friendsActivity.class);
        startActivity(intent);
    }
}
