package com.tomduan.conciseclock;

/**
 * Created by tomduan on 16-4-29.
 */
public class Item {

    private String startTime;
    private String endTime;
    private String message;

    public Item(String startTime, String endTime, String message) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.message = message;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getMessage() {
        return message;
    }
}
