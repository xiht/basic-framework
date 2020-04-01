package com.hunterxi.lib.model;

/**
 * @author HunterXi
 * 创建日期：2019/7/12 11:07
 * 描述：消息传递实体类
 */
public class RxbusEvent {

    private int eventInteger;
    private String eventString;

    public RxbusEvent(int eventInteger) {
        this.eventInteger = eventInteger;
    }

    public RxbusEvent(String eventString) {
        this.eventString = eventString;
    }

    public RxbusEvent(int eventInteger, String eventString) {
        this.eventInteger = eventInteger;
        this.eventString = eventString;
    }

    public int getEventInteger() {
        return eventInteger;
    }

    public void setEventInteger(int eventInteger) {
        this.eventInteger = eventInteger;
    }

    public String getEventString() {
        return eventString;
    }

    public void setEventString(String eventString) {
        this.eventString = eventString;
    }
}
