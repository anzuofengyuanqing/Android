package cn.edu.nuc.seeworld.utils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lenovo on 2015/10/14.
 */
public class StringUtils {
    public static List allprovince(){
        String[] s=new String[]{
                "北京","上海","天津","重庆","河北","河南","云南","辽宁",
                "黑龙江","湖南","安徽","山东","新疆","江苏","浙江","江西",
                "湖北","广西","甘肃","山西","内蒙古","陕西","吉林","福建",
                "贵州","广东","青海","西藏","四川","宁夏","海南","台湾",
                "香港","澳门"
        };
        List<String> list=Arrays.asList(s);
        return list;
    }


}
