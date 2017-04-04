package com.account.king.node;

import java.io.Serializable;

/**
 * Created by king
 * on 2016/11/20.
 */
public class Attachment implements Serializable {
    /**
     * content : 今天发工资哎
     * attachment_path : https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/logo_white_fe6da1ec.png
     */

    private String content;
    private String attachment_path;
    private String location ;
    public Attachment() {
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttachment_path() {
        return attachment_path;
    }

    public void setAttachment_path(String attachment_path) {
        this.attachment_path = attachment_path;
    }

    public Object copy() {
        Attachment attachment = new Attachment();
        attachment.setContent(this.content);
        attachment.setAttachment_path(this.attachment_path);
        return attachment;
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "content='" + content + '\'' +
                ", attachment_path='" + attachment_path + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
