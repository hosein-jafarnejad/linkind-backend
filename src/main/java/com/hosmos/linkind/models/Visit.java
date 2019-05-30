package com.hosmos.linkind.models;

import javax.xml.crypto.Data;

public class Visit {
    private String Ip;
    private Data date;
    private long link_id;
    private String browser_name;
    private String browser_version;
    private String os;

    public String getIp() {
        return Ip;
    }

    public void setIp(String ip) {
        Ip = ip;
    }

    public Data getDate() {
        return date;
    }

    public void setDate(Data date) {
        this.date = date;
    }

    public long getLink_id() {
        return link_id;
    }

    public void setLink_id(long link_id) {
        this.link_id = link_id;
    }

    public String getBrowser_name() {
        return browser_name;
    }

    public void setBrowser_name(String browser_name) {
        this.browser_name = browser_name;
    }

    public String getBrowser_version() {
        return browser_version;
    }

    public void setBrowser_version(String browser_version) {
        this.browser_version = browser_version;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }
}
