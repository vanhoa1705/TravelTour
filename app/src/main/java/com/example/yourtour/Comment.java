package com.example.yourtour;

import java.io.Serializable;

public class Comment implements Serializable {
    public String cmt_id, cmt_name, comment;

    public Comment(String cmt_id, String cmt_name, String comment){
        this.cmt_id = cmt_id;
        this.cmt_name = cmt_name;
        this.comment = comment;
    }

    public String getCmt_id() {
        return cmt_id;
    }

    public void setCmt_id(String cmt_id) {
        this.cmt_id = cmt_id;
    }

    public String getCmt_name() {
        return cmt_name;
    }

    public void setCmt_name(String cmt_name) {
        this.cmt_name = cmt_name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}