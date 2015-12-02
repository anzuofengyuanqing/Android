package cn.edu.nuc.download.entity;

/**
 * Created by lenovo on 2015/11/30.
 */
public class ThreadInfo {
    private int id;
    private String url;
    private int start;
    private int end;
    private int finish;

    public ThreadInfo() {
    }

    public ThreadInfo(int id, String url, int start, int end, int finish) {
        this.id = id;
        this.url = url;
        this.start = start;
        this.end = end;
        this.finish = finish;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getFinish() {
        return finish;
    }

    public void setFinish(int finish) {
        this.finish = finish;
    }

    @Override
    public String toString() {
        return "ThreadInfo{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", finish=" + finish +
                '}';
    }
}
