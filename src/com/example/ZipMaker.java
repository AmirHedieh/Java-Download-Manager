package com.example;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * export all program files like list.jdm,Settings.jdm,removed.jdm ,... to
 * one zip file located at project directory
 * file name is CompressedData.zip
 */
public class ZipMaker {

    public ZipMaker(){

        try {
            FileOutputStream fos = new FileOutputStream("CompressedData.zip");
            ZipOutputStream zos = new ZipOutputStream(fos);

            String file1Name = "Files//filter.jdm";
            String file2Name = "Files//list.jdm";
            String file3Name = "Files//queue.jdm";
            String file4Name = "Files//removed.jdm";
            String file5Name = "Files//Settings.jdm";

            addToZipFile(file1Name, zos);
            addToZipFile(file2Name, zos);
            addToZipFile(file3Name, zos);
            addToZipFile(file4Name, zos);
            addToZipFile(file5Name, zos);

            zos.close();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * write a file with "fileName" path to zip file using ZipOutputStream
     * @param fileName
     * @param zos
     * @throws IOException
     */
    public static void addToZipFile(String fileName, ZipOutputStream zos) throws IOException {

        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zos.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }

        zos.closeEntry();
        fis.close();
    }

}
