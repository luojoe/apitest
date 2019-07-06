package com.zhaow.restful.common;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * @author joe
 * @data 2019/7/6 23:50
 * @email joeluo520@gmail.com
 */
public class RespondTestEntity implements Serializable ,Comparable<RespondTestEntity>{
    private int status;
    private String url;
    private String param;
    private String respond;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getRespond() {
        return respond;
    }

    public void setRespond(String respond) {
        this.respond = respond;
    }

    @Override
    public int compareTo(@NotNull RespondTestEntity o) {
        if (this.status == o.getStatus()) {
            return 0;
        } else if (this.status > o.status) {
            return -1;
        } else {
            return 1;
        }
    }

    @Override
    public String toString() {
        return "\n" + this.status + "--RESPOND：" + this.getRespond() + ";URL：" + this.getUrl() + ";PARAM：" + getParam();
    }
}
