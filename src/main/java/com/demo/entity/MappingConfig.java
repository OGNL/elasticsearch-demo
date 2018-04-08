package com.demo.entity;

/**
 * ES mapping结构设置
 * Created by zhangtao on 2018/4/8.
 */
public class MappingConfig {

    private String fieldName;//字段名称
    private String fieldType;//字段类型，为ES中的类型
    private String analyzer;//分词器

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getAnalyzer() {
        return analyzer;
    }

    public void setAnalyzer(String analyzer) {
        this.analyzer = analyzer;
    }
}
