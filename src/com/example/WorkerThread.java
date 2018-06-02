package com.example;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WorkerThread implements Runnable{
    JFrame frame;
    Download download;
    String fileLink;
    String savePath;
    String name;
    boolean pause;
    boolean started = false;

    public WorkerThread(JFrame frame, Download download , String link, String savePath){
        this.frame = frame;
        this.download = download;
        this.fileLink = link;
        this.savePath = savePath;
    }
    @Override
    public void run() {
        try {
            downloadFromNet(frame,download,fileLink,savePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setName(String s){
        name = s;
    }

    public String getName() {
        return name;
    }

    private void downloadFromNet(JFrame frame,Download download,String fileURL, String saveDir) throws IOException {
        URL url = new URL(fileURL);
        try {
            System.out.println("Download Started!");
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            int responseCode = httpConn.getResponseCode();
//                System.out.println(responseCode);
            // always check HTTP response code first
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String fileName = "";
                String disposition = httpConn.getHeaderField("Content-Disposition");
                String contentType = httpConn.getContentType();
                int contentLength = httpConn.getContentLength(); //file size

                if (disposition != null) {
                    // extracts file name from header field
                    int index = disposition.indexOf("filename=");
                    if (index > 0) {
                        fileName = disposition.substring(index + 9,
                                disposition.length());
                    }
                } else {
                    // extracts file name from URL
                    fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
                }

                download.setSize(contentLength / 1000); // set file size in download object
                download.setName(fileName);

//                    System.out.println("Content-Disposition = " + disposition);
//                    System.out.println("Content-Length = " + contentLength);
                System.out.println("fileName = " + fileName);

                InputStream inputStream = httpConn.getInputStream();

                // opens input stream from the HTTP connection
//                    System.out.println(saveDir);
                String saveFilePath = saveDir + File.separator + fileName;

                // opens an output stream to save into file
                FileOutputStream outputStream = new FileOutputStream(saveFilePath);

                int bytesRead = -1;
                int bufferSize = contentLength / 100;
//                    System.out.println(bufferSize);
                byte[] buffer = new byte[4096];
                int totalRead = 0;
                int process = 0; //  between 0-100

                setStarted(true);

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    //System.out.println("-"+bytesRead);
                    outputStream.write(buffer, 0, bytesRead);
                    totalRead += bytesRead;
                    process = (totalRead * 100) / contentLength;
                    download.getProgressBar().setValue(process);
                    frame.repaint();
                    frame.revalidate();
                    while (pause){
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            System.out.println("Couldn't stop thread");
                        }
                    }
                }


                download.setCompleted(true);
                download.setInProgress(false);
                outputStream.close();
                inputStream.close();

                System.out.println("File downloaded");
            } else {
                System.out.println("No file to download. Server replied HTTP code: " + responseCode);
            }
            httpConn.disconnect();
        }
        catch (IOException ex){
            System.out.println("Wrong URL");
        }
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean state){
        pause = state;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }
}