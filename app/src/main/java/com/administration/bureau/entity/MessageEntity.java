package com.administration.bureau.entity;

import java.util.List;

/**
 * Created by omyrobin on 2017/4/19.
 */

public class MessageEntity {

    /**
     * total : 1
     * per_page : 10
     * current_page : 1
     * last_page : 1
     * next_page_url : null
     * prev_page_url : null
     * from : 1
     * to : 1
     * data : [{"id":"bb5cfd8b-00bc-4720-8428-3899b29ba62b","type":"App\\Notifications\\RegistrantNotice","notifiable_id":11,"notifiable_type":"App\\Models\\User","data":{"registrant_id":11,"message":"恭喜您的境外人员登记注册审核通过了！","type":0,"content":"","created_at":"2017-04-19 09:27:08"},"read_at":null,"created_at":"2017-04-19 09:27:08","updated_at":"2017-04-19 09:27:08"}]
     */

    private int total;
    private int per_page;
    private int current_page;
    private int last_page;
    private Object next_page_url;
    private Object prev_page_url;
    private int from;
    private int to;
    private List<DataBeanX> data;

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

    public List<DataBeanX> getData() {
        return data;
    }

    public void setData(List<DataBeanX> data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * id : bb5cfd8b-00bc-4720-8428-3899b29ba62b
         * type : App\Notifications\RegistrantNotice
         * notifiable_id : 11
         * notifiable_type : App\Models\User
         * data : {"registrant_id":11,"message":"恭喜您的境外人员登记注册审核通过了！","type":0,"content":"","created_at":"2017-04-19 09:27:08"}
         * read_at : null
         * created_at : 2017-04-19 09:27:08
         * updated_at : 2017-04-19 09:27:08
         */

        private String id;
        private String type;
        private int notifiable_id;
        private String notifiable_type;
        private DataBean data;
        private Object read_at;
        private String created_at;
        private String updated_at;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getNotifiable_id() {
            return notifiable_id;
        }

        public void setNotifiable_id(int notifiable_id) {
            this.notifiable_id = notifiable_id;
        }

        public String getNotifiable_type() {
            return notifiable_type;
        }

        public void setNotifiable_type(String notifiable_type) {
            this.notifiable_type = notifiable_type;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public Object getRead_at() {
            return read_at;
        }

        public void setRead_at(Object read_at) {
            this.read_at = read_at;
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

        public static class DataBean {
            /**
             * registrant_id : 11
             * message : 恭喜您的境外人员登记注册审核通过了！
             * type : 0
             * content :
             * created_at : 2017-04-19 09:27:08
             */

            private int registrant_id;
            private String message;
            private int type;
            private String content;
            private String created_at;

            public int getRegistrant_id() {
                return registrant_id;
            }

            public void setRegistrant_id(int registrant_id) {
                this.registrant_id = registrant_id;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
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
        }
    }
}
