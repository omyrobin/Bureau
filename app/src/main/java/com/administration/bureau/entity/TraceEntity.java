package com.administration.bureau.entity;

import java.util.List;

public class TraceEntity {

    /**
     * keep_track : false
     * position : [["116.42093628644943237","40.03948579706933941"]]
     */

    private boolean keep_track;
    private List<List<String>> position;

    public boolean isKeep_track() {
        return keep_track;
    }

    public void setKeep_track(boolean keep_track) {
        this.keep_track = keep_track;
    }

    public List<List<String>> getPosition() {
        return position;
    }

    public void setPosition(List<List<String>> position) {
        this.position = position;
    }
}
