package cn.edu.nuc.download;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.edu.nuc.download.entity.FileInfo;
import cn.edu.nuc.download.services.DownloadService;

public class MainActivity extends AppCompatActivity {
    TextView mTVfilename;
    ProgressBar mPb;
    Button mBtnstart;
    Button mBtnstop;
    FileInfo fileInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTVfilename = (TextView) findViewById(R.id.tv_name);
        mPb = (ProgressBar) findViewById(R.id.pb);
        mBtnstart = (Button) findViewById(R.id.btn_download);
        mBtnstop = (Button) findViewById(R.id.btn_stop);
        fileInfo = new FileInfo(0, "http://10.253.0.254/files/1179000000006024/www.imooc.com/mobile/mukewang.apk",
                "mukewang.apk", 0, 0);
        mPb.setMax(100);
        mBtnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DownloadService.class);
                intent.setAction("ACTION_START");
                intent.putExtra("fileinfo", fileInfo);
                startService(intent);
            }
        });
        mBtnstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DownloadService.class);
                intent.setAction("ACTION_STOP");
                intent.putExtra("fileinfo", fileInfo);
                startService(intent);
            }
        });
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadService.ACTION_UPDATE);
        registerReceiver(broadcastReceiver, filter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (DownloadService.ACTION_UPDATE.equals(intent.getAction())) {
                int finished = intent.getIntExtra("finished", 0);
                fileInfo.setFinished(finished);
                mPb.setProgress(finished);
            }
        }
    };
}
