package com.administration.bureau.entity;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 * Created by omyrobin on 2017/4/18.
 */

public class DataEntity implements Comparable{

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

    public String getFirstAlphabet() {
        return value.subSequence(0, 1).toString();
    }

    @Override
    public int compareTo(@NonNull Object another) {
        return getFirstAlphabet().compareTo(((DataEntity) another).getFirstAlphabet());
    }
}
