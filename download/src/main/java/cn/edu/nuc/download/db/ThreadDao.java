package cn.edu.nuc.download.db;

import java.util.List;

import cn.edu.nuc.download.entity.ThreadInfo;

/**
 * Created by lenovo on 2015/12/1.
 */
public interface ThreadDao {
    public void insertThread(ThreadInfo threadInfo);

    public void deleteThread(String Url, int thread_id);

    public void updateThread(String Url, int thread_id, int finished);

    public List<ThreadInfo> getThreads(String Url);

    public boolean isexists(String Url, int thread_id);

}
