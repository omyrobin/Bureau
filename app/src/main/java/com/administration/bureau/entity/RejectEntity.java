package com.administration.bureau.entity;

import java.io.Serializable;

/**
 * Created by wubo on 2018/5/7.
 */

public class RejectEntity implements Serializable{
    private String label;

    private String value;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
