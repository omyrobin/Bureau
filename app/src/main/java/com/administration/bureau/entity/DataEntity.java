package com.administration.bureau.entity;

/**
 * Created by omyrobin on 2017/4/18.
 */

public class DataEntity {

    private String key;

    private String value;

    public DataEntity(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return "DataEntity{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
