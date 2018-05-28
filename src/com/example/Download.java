package com.example;

import java.io.File;

public class Download {
    //fields
    private String link;
    private String name;
    private long size;

    public Download(String link, String name){
        this.link = link;
        this.name = name;

    }

    //methods

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    private void setSize(){
        File file = new File(link);
        size = file.length() / 100;
    }
}
