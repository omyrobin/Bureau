package com.administration.bureau.entity;

/**
 * Created by omyrobin on 2017/4/20.
 */

public class ArticleDetialEntity {

    /**
     * id : 1
     * title : 测试1
     * cover :
     * author :
     * type : 1
     * recommend : 0
     * language : 2
     * order : 1
     * intro :
     * content : <p>阿萨德飞洒</p>
     * created_at : 2017-04-07 12:29:32
     * updated_at : 2017-04-07 12:58:57
     */

    private int id;
    private String title;
    private String cover;
    private String author;
    private int type;
    private int recommend;
    private int language;
    private int order;
    private String intro;
    private String content;
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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
