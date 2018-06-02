package com.example;

import javax.swing.*;
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
    private boolean inProgress = false;
    private boolean inQueue = false;
    private boolean completed = false;
    private int QueueStartMinute = 0 ;
    private int QueueStartHour = 0;
    private JProgressBar progressBar = new JProgressBar(0,100);

    public Download(String link,String time){
        this.link = link;
        makeProgressBar();
        this.time = time;
        //setSize();
    }

    //methods


    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    private void makeProgressBar(){
        progressBar.setSize(500,20);
        progressBar.setLocation(70,34);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size){
        this.size = size;
    }

    public void setName(String name) {
        this.name = name;
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

    public boolean isCompleted() {
        return completed;
    }
}
