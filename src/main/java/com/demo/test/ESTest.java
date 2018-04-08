package com.demo.test;

import com.demo.entity.MappingConfig;
import com.demo.util.ESUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangtao on 2018/3/6.
 */
public class ESTest {

    public static void main(String [] args){

//        Map<String,Object> paramMap = new HashMap<String, Object>();
//        paramMap.put("name","小明");
//        paramMap.put("age",19);
//        paramMap.put("sex","女");
//        paramMap.put("city","北京");
//        paramMap.put("address","江苏省无锡市滨湖区xxx街道36号");
//        paramMap.put("telephone","12345678911");
//        paramMap.put("education","初中");
//        paramMap.put("email","123456@123.com");
//        ESUtil.addMapToES("user","user",paramMap);
//        ESUtil.updateById("user","user","AWKAFRK-Xqv6FXbMvKh9",paramMap);
//        ESUtil.deleteById("user","user2","AWKAd3FMXqv6FXbMvKiI");
       // Map map = ESUtil.selectById("user","user2","AWKAg8p0Xqv6FXbMvKiW");
//        List<Map<String,Object>> mapList = ESUtil.termQueryByKeyword("user","user","city.raw","上海");
//        List<Map<String,Object>> mapList = ESUtil.matchQueryByKeyword("user","user","city","江南",true);
//        System.out.println("123456");

//        ESUtil.createIndex("user");
        MappingConfig m1 = new MappingConfig();
        m1.setFieldName("name");
        m1.setFieldType("text");
        m1.setAnalyzer("ik_max_word");
        MappingConfig m2 = new MappingConfig();
        m2.setFieldName("sex");
        m2.setFieldType("text");
        m2.setAnalyzer("ik_max_word");
        MappingConfig m3 = new MappingConfig();
        m3.setFieldName("age");
        m3.setFieldType("integer");
        MappingConfig m4 = new MappingConfig();
        m4.setFieldName("introduction");
        m4.setFieldType("text");
        m4.setAnalyzer("ik_max_word");
        MappingConfig m5 = new MappingConfig();
        m5.setFieldName("address");
        m5.setFieldType("text");
        m5.setAnalyzer("ik_max_word");
        MappingConfig m6 = new MappingConfig();
        m6.setFieldName("city");
        m6.setFieldType("text");
        m6.setAnalyzer("ik_max_word");
        MappingConfig m7 = new MappingConfig();
        m7.setFieldName("email");
        m7.setFieldType("text");
        m7.setAnalyzer("ik_max_word");
        MappingConfig m8 = new MappingConfig();
        m8.setFieldName("education");
        m8.setFieldType("text");
        m8.setAnalyzer("ik_max_word");
        List<MappingConfig> mappingConfigList = new ArrayList<MappingConfig>();
        mappingConfigList.add(m1);
        mappingConfigList.add(m2);
        mappingConfigList.add(m3);
        mappingConfigList.add(m4);
        mappingConfigList.add(m5);
        mappingConfigList.add(m6);
        mappingConfigList.add(m7);
        mappingConfigList.add(m8);
        ESUtil.createMapping("user","user2",mappingConfigList);

    }






}
