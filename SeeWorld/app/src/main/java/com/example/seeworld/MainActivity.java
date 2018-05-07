package com.example.seeworld;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;

import java.security.MessageDigest;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TencentLocationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkCertificate(MainActivity.this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {

    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    /**
     * 获取md5指纹信息(签名文件不同,md5指纹也不同;不同包名app可以具有相同的md5指纹信息)
     * 示例输出:
     * sign: 55:2e:ba:e6:b4:7e:ac:e3:02:58:64:9a:db:82:87:b6
     */
    private void checkCertificate(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            Signature sig = pm.getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES).signatures[0];
            String md5Fingerprint = doFingerprint(sig.toByteArray(), "MD5");
            Log.d("sign:", md5Fingerprint);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param certificateBytes 获取到应用的signature值
     * @param algorithm        在上文指定MD5算法
     * @return md5签名
     */
    private String doFingerprint(byte[] certificateBytes, String algorithm) throws Exception {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(certificateBytes);
        byte[] digest = md.digest();

        String toRet = "";
        for (int i = 0; i < digest.length; i++) {
            if (i != 0) {
                toRet += ":";
            }
            int b = digest[i] & 0xff;
            String hex = Integer.toHexString(b);
            if (hex.length() == 1) {
                toRet += "0";
            }
            toRet += hex;
        }
        return toRet;
    }
}
