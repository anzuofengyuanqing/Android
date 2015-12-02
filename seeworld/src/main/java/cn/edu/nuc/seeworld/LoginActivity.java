package cn.edu.nuc.seeworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;

import cn.bmob.v3.listener.SaveListener;
import cn.edu.nuc.seeworld.entity.MyUser;

/**
 * Created by lenovo on 2015/9/12.
 */
public class LoginActivity extends Activity {
    private CircularProgressButton main;
    private TextView register;
    private EditText etphonenum;
    private EditText etpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        etphonenum= (EditText) findViewById(R.id.et_phonenum);
        etpassword= (EditText) findViewById(R.id.et_password);
        main= (CircularProgressButton) findViewById(R.id.btn_login);
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUser myUser=new MyUser();
                myUser.setUsername(etphonenum.getText().toString());
                myUser.setPassword(etpassword.getText().toString());
                Config.CurrUser=myUser;
                myUser.login(LoginActivity.this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        main.setProgress(100);
                        Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        main.setProgress(0);
                        // TODO Auto-generated method stub
                        Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        register= (TextView) findViewById(R.id.register_link);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
