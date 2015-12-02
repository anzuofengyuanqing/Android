package cn.edu.nuc.seeworld.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by lenovo on 2015/9/14.
 */
public class MyMessage extends BmobObject {
    private String contenttext;
    private String StreeID;
    private MyUser user;

    public String getContenttext() {
        return contenttext;
    }

    public void setContenttext(String contenttext) {
        this.contenttext = contenttext;
    }

    public String getStreeID() {
        return StreeID;
    }

    public void setStreeID(String streeID) {
        StreeID = streeID;
    }

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }


}
