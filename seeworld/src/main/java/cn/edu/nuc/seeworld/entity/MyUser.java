package cn.edu.nuc.seeworld.entity;

import cn.bmob.v3.BmobUser;

/**
 * Created by lenovo on 2015/9/13.
 */
public class MyUser extends BmobUser {
        private Boolean sex;
        private Integer age;

    public Integer getAge() {
        return age;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setAge(Integer age) {

        this.age = age;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }
}
