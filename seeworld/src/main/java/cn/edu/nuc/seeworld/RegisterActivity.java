package cn.edu.nuc.seeworld;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.sms.BmobSMS;
import cn.bmob.v3.listener.SaveListener;
import cn.edu.nuc.seeworld.entity.MyUser;

/**
 * Created by lenovo on 2015/9/13.
 */
public class RegisterActivity extends Activity {
    Button yzbtn;
    EditText etphonenum;
    EditText etpassword;
    EditText etyzm;
    Button register;
    String phonenum;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        yzbtn= (Button) findViewById(R.id.btn_yz);
        register= (Button) findViewById(R.id.btn_register);
        etphonenum= (EditText) findViewById(R.id.et_phonenum);
        etpassword= (EditText) findViewById(R.id.et_password);
        etyzm= (EditText) findViewById(R.id.et_yzm);
        phonenum=etphonenum.getText().toString();
        password=etpassword.getText().toString();
        yzbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getyzm();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registeruser();
            }
        });

    }
    public void getyzm() {
        BmobSMS.requestSMSCode(this, etphonenum.getText().toString(), "注册验证码", new cn.bmob.sms.listener.RequestSMSCodeListener() {
            @Override
            public void done(Integer integer, cn.bmob.sms.exception.BmobException e) {
                if (e == null) {//验证码发送成功
                    Log.i("smile", "短信id：" + integer);//用于查询本次短信发送详情
                }
            }
        });
    }
    public  void registeruser(){
        final MyUser user = new MyUser();
        user.setMobilePhoneNumber(etphonenum.getText().toString());//设置手机号码（必填）
//        user.setUsername(xxx);                  //设置用户名，如果没有传用户名，则默认为手机号码
        user.setPassword(etpassword.getText().toString());                  //设置用户密码
        user.setAge(18);                       //设置额外信息：此处为年龄
        user.setSex(true);
        user.signOrLogin(this,etyzm.getText().toString() , new SaveListener() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                Config.CurrUser=user;
                Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(int code, String msg) {
                // TODO Auto-generated method stub
                Toast.makeText(RegisterActivity.this, "错误码："+code+",错误原因："+msg, Toast.LENGTH_SHORT).show();
            }
        });

    }
}