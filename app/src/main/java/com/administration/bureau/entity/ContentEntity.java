package com.administration.bureau.entity;

/**
 * Created by omyrobin on 2017/4/21.
 */

public class ContentEntity {

    /**
     * content : 内容内容
     * user_id : 51
     * updated_at : 2017-04-14 13:20:40
     * created_at : 2017-04-14 13:20:40
     * id : 31
     */

    private String content;
    private int user_id;
    private String updated_at;
    private String created_at;
    private int id;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
