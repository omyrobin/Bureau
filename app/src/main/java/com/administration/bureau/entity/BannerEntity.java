package com.administration.bureau.entity;

/**
 * Created by omyrobin on 2017/4/14.
 */

public class BannerEntity {

    /**
     * id : 1
     * title :
     * image : http://www.guibenchuxin.com:7070/ueditor/php/upload/image/20170411/1491874332254679.jpeg
     * url : http://police.bjfsh.gov.cn
     * order : 1
     * created_at : 2017-04-11 09:32:55
     * updated_at : 2017-04-11 09:32:55
     */

    private int id;
    private String title;
    private String image;
    private String url;
    private int order;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
