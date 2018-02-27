package com.administration.bureau.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by omyrobin on 2017/4/14.
 */

public class ArticleEntity implements Serializable{

    /**
     * total : 2
     * per_page : 10
     * current_page : 1
     * last_page : 1
     * next_page_url : null
     * prev_page_url : null
     * from : 1
     * to : 2
     * data : [{"id":1,"title":"房山公安分局党委书记、分局长李宝虎当选为房山区人民政府副区长","author":"","type":4,"order":1,"recommend":1,"cover":"","language":0,"created_at":"2017-04-11 09:37:59"},{"id":2,"title":"房山分局向市、区人大代表汇报工作并征求意见建议","author":"","type":4,"order":2,"recommend":1,"cover":"","language":0,"created_at":"2017-04-11 09:39:37"}]
     */

    private int total;
    private int per_page;
    private int current_page;
    private int last_page;
    private Object next_page_url;
    private Object prev_page_url;
    private int from;
    private int to;
    private ArrayList<DataBean> data;
    private ArrayList<BannerEntity> banners;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public Object getNext_page_url() {
        return next_page_url;
    }

    public void setNext_page_url(Object next_page_url) {
        this.next_page_url = next_page_url;
    }

    public Object getPrev_page_url() {
        return prev_page_url;
    }

    public void setPrev_page_url(Object prev_page_url) {
        this.prev_page_url = prev_page_url;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public ArrayList<DataBean> getData() {
        return data;
    }

    public void setData(ArrayList<DataBean> data) {
        this.data = data;
    }

    public ArrayList<BannerEntity> getBanners() {
        return banners;
    }

    public void setBanners(ArrayList<BannerEntity> banners) {
        this.banners = banners;
    }

    public static class DataBean implements Serializable{
        /**
         * id : 1
         * title : 房山公安分局党委书记、分局长李宝虎当选为房山区人民政府副区长
         * author :
         * type : 4
         * order : 1
         * recommend : 1
         * cover :
         * language : 0
         * created_at : 2017-04-11 09:37:59
         * updated_at : 2017-04-06 23:48:08
         */

        private int id;
        private String title;
        private String author;
        private int type;
        private int order;
        private int recommend;
        private String cover;
        private int language;
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

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public int getRecommend() {
            return recommend;
        }

        public void setRecommend(int recommend) {
            this.recommend = recommend;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public int getLanguage() {
            return language;
        }

        public void setLanguage(int language) {
            this.language = language;
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
}
