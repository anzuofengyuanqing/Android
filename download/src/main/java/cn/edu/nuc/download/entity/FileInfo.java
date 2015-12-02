package cn.edu.nuc.download.entity;

import java.io.Serializable;

/**
 * Created by lenovo on 2015/11/30.
 */
public class FileInfo implements Serializable {
    private int id;
    private String url;
    private String Filename;
    private int length;
    private int finished;

    public FileInfo() {
    }

    public FileInfo(int id, String url, String filename, int length, int finished) {

        this.id = id;
        this.url = url;
        Filename = filename;
        this.length = length;
        this.finished = finished;
    }

    public String getFilename() {

        return Filename;
    }

    public void setFilename(String filename) {
        Filename = filename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", Filename='" + Filename + '\'' +
                ", length=" + length +
                ", finished=" + finished +
                '}';
    }
}
