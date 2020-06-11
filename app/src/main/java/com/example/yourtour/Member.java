package com.example.yourtour;

import java.io.Serializable;

public class Member implements Serializable {
    public String mb_id, mb_name, mb_phone;

    public Member(String mb_id, String mb_name, String mb_phone){
        this.mb_id = mb_id;
        this.mb_name = mb_name;
        this.mb_phone = mb_phone;
    }

    public String getMb_id() {
        return mb_id;
    }

    public void setMb_id(String mb_id) {
        this.mb_id = mb_id;
    }

    public String getMb_name() {
        return mb_name;
    }

    public void setMb_name(String mb_name) {
        this.mb_name = mb_name;
    }

    public String getMb_phone() {
        return mb_phone;
    }

    public void setMb_phone(String mb_phone) {
        this.mb_phone = mb_phone;
    }
}
