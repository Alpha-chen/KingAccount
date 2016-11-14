package com.account.king;

/**
 * Created by xupangen
 * on 2016/10/15.
 */
public class FeedNode {

    private int id;
    public static String ID="id";
    /**
     *
     * 心情
     */
    private String mood ;
    public static  String MOOD="mood";
    /**
     * 评论的内容
     */
    private String  comment ;
    public static  String COMMENT = "comment";
    /**
     * 心情或评论的类型
     */
    private int feedStatus ;
    public static String FEEDSTATUS="feedstatus";
    /**
     * 关联的对象，具体是短链接和网址，
     */
    private String link ;
    public static String LINK = "link";

    public FeedNode() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getFeedStatus() {
        return feedStatus;
    }

    public void setFeedStatus(int feedStatus) {
        this.feedStatus = feedStatus;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
