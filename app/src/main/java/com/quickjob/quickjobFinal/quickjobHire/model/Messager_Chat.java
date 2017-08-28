package com.quickjob.quickjobFinal.quickjobHire.model;

/**
 * Created by VinhNguyen on 4/11/2017.
 */

public class Messager_Chat {
    public String avata;
    public String messager;
    public String name;
    public String uid;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String time;
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMessager() {
        return messager;
    }

    public void setMessager(String messager) {
        this.messager = messager;
    }

    public Messager_Chat(){

    }
    public String getAvata() {
        return avata;
    }

    public void setAvata(String avata) {
        this.avata = avata;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public boolean isView;

    public boolean isView() {
        return isView;
    }

    public void setView(boolean view) {
        isView = view;
    }
}
