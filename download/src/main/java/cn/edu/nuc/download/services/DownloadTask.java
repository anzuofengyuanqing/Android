package cn.edu.nuc.download.services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.apache.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import cn.edu.nuc.download.db.ThreadDao;
import cn.edu.nuc.download.db.ThreadDaoImpl;
import cn.edu.nuc.download.entity.FileInfo;
import cn.edu.nuc.download.entity.ThreadInfo;

/**
 * Created by lenovo on 2015/12/1.
 */
public class DownloadTask {
    private Context context = null;
    private FileInfo fileInfo = null;
    private ThreadDao threadDao;
    private int finished = 0;
    public boolean isPause = false;

    public DownloadTask(Context context, FileInfo fileInfo) {
        this.context = context;
        this.fileInfo = fileInfo;
        this.threadDao = new ThreadDaoImpl(context);
    }

    public void download() {
        List<ThreadInfo> threadInfos = threadDao.getThreads(fileInfo.getUrl());
        ThreadInfo threadInfo = null;
        if (threadInfos.size() == 0) {
            threadInfo = new ThreadInfo(0, fileInfo.getUrl(), 0, fileInfo.getLength(), 0);
        } else {
            Log.i("DownloadService", "断点开始");
            threadInfo = threadInfos.get(0);
        }
        new DownloadThread(threadInfo).start();
    }

    class DownloadThread extends Thread {
        private ThreadInfo threadInfo = null;

        public DownloadThread(ThreadInfo threadInfo) {
            this.threadInfo = threadInfo;
        }

        @Override
        public void run() {
            Log.i("DownloadService", "++++++++++++开始下载");
            if (!threadDao.isexists(threadInfo.getUrl(), threadInfo.getId())) {
                Log.i("DownloadService", "++++++++++++插入准备");
                threadDao.insertThread(threadInfo);
                Log.i("DownloadService", "++++++++++++完成准备");
            }
            HttpURLConnection conn = null;
            RandomAccessFile raf = null;
            InputStream in = null;
            Log.i("DownloadService", "++++++++++++连接");
            try {
                URL url = new URL(threadInfo.getUrl());
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(3000);
                int start = threadInfo.getStart() + threadInfo.getFinish();
                conn.setRequestProperty("Range", "bytes=" + start + "-" + threadInfo.getEnd());
                File file = new File(DownloadService.DOWNLOAD_PATH, fileInfo.getFilename());
                raf = new RandomAccessFile(file, "rwd");
                raf.seek(start);
                Intent intent = new Intent(DownloadService.ACTION_UPDATE);
                finished += threadInfo.getFinish();
                Log.i("DownloadService", "++++++++++++连接准备" + finished);
                if (conn.getResponseCode() == HttpStatus.SC_PARTIAL_CONTENT) {
                    Log.i("DownloadService", "++++++++++++连接通过");
                    in = conn.getInputStream();
                    byte[] buffer = new byte[1024];
                    int len = -1;
                    long time = System.currentTimeMillis();
                    while ((len = in.read(buffer)) != -1) {
                        raf.write(buffer, 0, len);
                        finished += len;
                        if (System.currentTimeMillis() - time > 500) {
                            time = System.currentTimeMillis();
                            intent.putExtra("finished", finished * 100 / fileInfo.getLength());
                            context.sendBroadcast(intent);
                        }
                        if (isPause) {
                            Log.i("DownloadService", "==========================停");
                            threadDao.updateThread(threadInfo.getUrl(), threadInfo.getId(), finished);
                            return;
                        }
                    }
                    threadDao.deleteThread(threadInfo.getUrl(), threadInfo.getId());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                    conn.disconnect();
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
