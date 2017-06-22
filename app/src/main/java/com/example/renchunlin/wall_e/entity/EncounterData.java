package com.example.renchunlin.wall_e.entity;

/**
 * 项目名：   WALL_E
 * 包名：     com.example.renchunlin.wall_e.ui;
 * 文件名：   EncounterData
 * 创建者：   RCL
 * 创建时间： 2016/8/6 9:17
 * 描述：     偶遇的实体类
 */
public class EncounterData {

    //标题
    private String title;
    //出处
    private String description;
    //图片的URL
    private String picUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }


    @Override
    public String toString() {
        return "WeChatData{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", picUrl='" + picUrl + '\'' +
                '}';
    }
}
