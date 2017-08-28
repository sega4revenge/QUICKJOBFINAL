package com.quickjob.quickjobFinal.quickjobFind.model;

/**
 * Created by VinhNguyen on 4/11/2017.
 */

public class Messager_Chat {
    public String avata;
    public  String messager;
    public  String name;
    public  String uid;
    public String statusInbox;

    public String getNotseen() {
        return notseen;
    }

    public void setNotseen(String notseen) {
        this.notseen = notseen;
    }

    public String notseen;
    public String getStatusInbox() {
        return statusInbox;
    }

    public void setStatusInbox(String statusInbox) {
        this.statusInbox = statusInbox;
    }

    public String getNewchat() {
        return newchat;
    }

    public void setNewchat(String newchat) {
        this.newchat = newchat;
    }

    public  String newchat;
    public String getTimenew() {
        return timenew;
    }

    public void setTimenew(String timenew) {
        this.timenew = timenew;
    }

    public  String timenew;
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public  String time;
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

    public boolean isview() {
        return isview;
    }

    public void setIsview(boolean isview) {
        this.isview = isview;
    }

    public boolean isview;
}
