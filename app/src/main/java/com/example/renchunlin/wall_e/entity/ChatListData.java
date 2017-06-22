package com.example.renchunlin.wall_e.entity;

/**
 * 项目名：   WALL_E
 * 包名：     com.example.renchunlin.wall_e.ui;
 * 文件名：   ChatListData
 * 创建者：   RCL
 * 创建时间： 2016/10/5 20:11
 * 描述：     对话列表的实体
 */
public class ChatListData {
    //type
    private int type;
    //文本
    private String text;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "ChatListData{" +
                "type=" + type +
                ", text='" + text + '\'' +
                '}';
    }
}
