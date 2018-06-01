package com.example;

import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

    /**
     * A utility that downloads a file from a URL.
     */
    public class Downloader {

        /**
         * Downloads a file from a URL
         * @param fileURL HTTP URL of the file to be downloaded
         * @param saveDir path of the directory to save the file
         * @throws IOException
         */
        public static void downloadFromNet(JFrame frame,Download download,String fileURL, String saveDir) throws IOException {
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

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        //System.out.println("-"+bytesRead);
                        outputStream.write(buffer, 0, bytesRead);
                        totalRead += bytesRead;
                        process = (totalRead * 100) / contentLength;
                        download.getProgressBar().setValue(process);
                        frame.repaint();
                        frame.revalidate();
                    }


                    download.setCompleted(true);
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
    }

