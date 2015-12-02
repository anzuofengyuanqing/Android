package cn.edu.nuc.download.services;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import cn.edu.nuc.download.entity.FileInfo;

/**
 * Created by lenovo on 2015/11/30.
 */
public class DownloadService extends Service {
    //sdcard
    public static final String TAG = "DownloadService";
    public static final String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/downloads/";
    public static final String ACTION_START = "ACTION_START";
    public static final String ACTION_STOP = "ACTION_STOP";
    public static final String ACTION_UPDATE = "ACTION_UPDATE";
    public static final int MSGINIT = 0x12;
    private DownloadTask downloadTask;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ACTION_START.equals(intent.getAction())) {
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileinfo");
            Log.i("DownloadService", "Start" + fileInfo.toString());
            new InitThread(fileInfo).start();
            if (downloadTask != null) {
                downloadTask.isPause = false;
            }
        } else if (ACTION_STOP.equals(intent.getAction())) {
            FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileinfo");
            Log.i("DownloadService", "Stop" + fileInfo.toString());
            if (downloadTask != null) {
                downloadTask.isPause = true;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSGINIT:
                    FileInfo fileInfo = (FileInfo) msg.obj;
                    Log.i("DownloadService", fileInfo.getLength() + "");
                    downloadTask = new DownloadTask(DownloadService.this, fileInfo);
                    downloadTask.download();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class InitThread extends Thread {
        private FileInfo mfileInfo;

        public InitThread(FileInfo fileInfo) {
            this.mfileInfo = fileInfo;
        }

        @Override
        public void run() {
            HttpURLConnection conn = null;
            RandomAccessFile randomAccessFile = null;
            try {
                URL url = new URL(mfileInfo.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(3000);
                int length = -1;
                if (conn.getResponseCode() == HttpStatus.SC_OK) {
                    length = conn.getContentLength();
                }
                if (length <= 0) {
                    return;
                }
                File dir = new File(DOWNLOAD_PATH);
                if (!dir.exists()) {
                    dir.mkdirs();

                }
                File file = new File(dir, mfileInfo.getFilename());
                randomAccessFile = new RandomAccessFile(file, "rwd");
                randomAccessFile.setLength(length);
                mfileInfo.setLength(length);
                mHandler.sendMessage(mHandler.obtainMessage(MSGINIT, mfileInfo));
//                /storage/sdcard0/downloads/mukewang.apk

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    conn.disconnect();
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }
    }
}
