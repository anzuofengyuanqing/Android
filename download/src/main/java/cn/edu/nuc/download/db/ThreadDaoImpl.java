package cn.edu.nuc.download.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nuc.download.entity.ThreadInfo;

/**
 * Created by lenovo on 2015/12/1.
 */
public class ThreadDaoImpl implements ThreadDao {
    private DBHelper dbHelper = null;

    public ThreadDaoImpl(Context context) {
        dbHelper = new DBHelper(context);
    }

    @Override
    public void insertThread(ThreadInfo threadInfo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("insert into thread_info(thread_id,url,start,end,finished) values(?,?,?,?,?)", new Object[]{
                threadInfo.getId(), threadInfo.getUrl(), threadInfo.getStart(), threadInfo.getEnd(), threadInfo.getFinish()});
        db.close();
    }

    @Override
    public void deleteThread(String Url, int thread_id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from thread_info where url=? and thread_id=?", new Object[]{Url, thread_id});
        db.close();
    }

    @Override
    public void updateThread(String Url, int thread_id, int finished) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("update thread_info set finished=? where url=? and thread_id=?", new Object[]{finished, Url, thread_id});
        db.close();
    }

    @Override
    public List<ThreadInfo> getThreads(String Url) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from thread_info where url=?", new String[]{Url});
        List<ThreadInfo> threadInfoes = new ArrayList<>();
        while (cursor.moveToNext()) {
            ThreadInfo threadInfo = new ThreadInfo();
            threadInfo.setId(cursor.getInt(cursor.getColumnIndex("thread_id")));
            threadInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            threadInfo.setStart(cursor.getInt(cursor.getColumnIndex("start")));
            threadInfo.setEnd(cursor.getInt(cursor.getColumnIndex("end")));
            threadInfo.setFinish(cursor.getInt(cursor.getColumnIndex("finished")));
            threadInfoes.add(threadInfo);
        }
        Log.i("DownloadService", threadInfoes.size() + "=================");
        db.close();
        return threadInfoes;
    }

    @Override
    public boolean isexists(String Url, int thread_id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from thread_info where url=? and thread_id=?", new String[]{Url, thread_id + ""});
        boolean exists = cursor.moveToNext();
        cursor.close();
        db.close();
        return exists;
    }
}
