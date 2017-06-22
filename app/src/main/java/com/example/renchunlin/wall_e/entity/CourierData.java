package com.example.renchunlin.wall_e.entity;

/*
 *  项目名:     WALL_E
 *  包名:       com.example.renchunlin.wall_e.ui;
 *  文件名:     CourierData
 *  创建者:     RCL
 *  创建时间:   2016/10/28 22:51
 *  描述:       快递查询实体
 */
public class CourierData {
    //时间
    private String time;
    //城市
    private String content;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CourierData{" +
                "time='" + time + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
