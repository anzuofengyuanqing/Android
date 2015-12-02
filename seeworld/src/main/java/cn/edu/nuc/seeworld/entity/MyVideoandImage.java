package cn.edu.nuc.seeworld.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by lenovo on 2015/10/8.
 */
public class MyVideoandImage extends BmobObject {
        private String text;//上传文字

        public String getProvince() {
                return province;
        }

        public void setProvince(String province) {
                this.province = province;
        }

        public String getText() {
                return text;
        }

        public void setText(String text) {
                this.text = text;
        }

        public String getImage_url() {
                return image_url;
        }

        public void setImage_url(String image_url) {
                this.image_url = image_url;
        }

        public String getVideo_url() {
                return video_url;
        }

        public void setVideo_url(String video_url) {
                this.video_url = video_url;
        }

        private String province;//所属省份
        private String image_url;//上传图片
        private String video_url;//上传视频

}
