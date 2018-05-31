package com.example;

import java.io.File;

/**
 * each download has fields containing crucial data and info about that like link , file name ,
 * or if it is in queue or not
 * completed field is true when file is downloaded successfully
 * inQueue field is true when the download is added to queue
 * queue timing is available if the download is added to queue
 */
public class Download {
    //fields
    private String link;
    private String name;
    private long size;
    private String time;
    private boolean inQueue = false;
    private boolean completed = false;
    private int QueueStartMinute = 0 ;
    private int QueueStartHour = 0;

    public Download(String link, String name,String time){
        this.link = link;
        this.name = name;
        this.time = time;
        setSize();
    }

    //methods

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public long getSize() {
        return size;
    }

    private void setSize(){
        File file = new File(link);
        size = file.length() / 1000;
    }


    public String getTime(){
        return time;
    }

    public void setCompleted(Boolean b){
        completed = b;
    }

    public Boolean getCompleted(){
        return completed;
    }

    public int getQueueStartHour() {
        return QueueStartHour;
    }

    public int getQueueStartMinute() {
        return QueueStartMinute;
    }

    public void setQueueStartHour(int queueStartHour) {
        QueueStartHour = queueStartHour;
    }

    public void setQueueStartMinute(int queueStartMinute) {
        QueueStartMinute = queueStartMinute;
    }

    public boolean getisInQueue() {
        return inQueue;
    }

    public void setInQueue(boolean inQueue) {
        this.inQueue = inQueue;
    }
}
