package com.demo.test;

import com.demo.util.ESUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangtao on 2018/3/6.
 */
public class ESTest {

    public static void main(String [] args){

        Map<String,Object> paramMap = new HashMap<String, Object>();
        paramMap.put("name","小明");
        paramMap.put("age",17);
        paramMap.put("sex","男");
        paramMap.put("city","江苏无锡");
        paramMap.put("address","江苏省无锡市滨湖区xxx街道36号");
        paramMap.put("telephone","12345678911");

        ESUtil.addMapToES("user","user2",paramMap);

    }






}
