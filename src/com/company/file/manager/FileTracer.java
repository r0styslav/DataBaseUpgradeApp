package com.company.file.manager;

import java.io.*;

/**
 * Created by Rostyslav.Pash on 22-Feb-16.
 */

//C:\!Work\copy Test\dir1
//C:\!Work\copy Test\dir2

public class FileTracer {
    File srcDir;
    File destDir;
    File[] filesList;

    public FileTracer(String srcDir, String destDir) {
        this.srcDir = new File(srcDir);
        this.destDir = new File(destDir);
        this.filesList = getListOfFiles();
    }

    public void printListOfFiles() {
        for (File file : filesList) {
            if (file.isFile()) {
                System.out.println("File: " + file.getName());
            }
        }
    }
    public void printListOfFiles(String filePath) {
        File fileDir = new File(filePath);
        for (File file : fileDir.listFiles()) {
            if (file.isFile()) {
                System.out.println("File: " + file.getName());
            }
        }
    }

    public File[] getListOfFiles() {
        filesList = srcDir.listFiles();
        return filesList;
    }

    public void copyAllFiles() {
        copyAllFiles(srcDir, destDir);
    }

    public void copyAllFiles(File srcDir, File destDir) {
        for (File file : filesList) {
            try {
                InputStream in = new FileInputStream(file);
                OutputStream out = new FileOutputStream(destDir + "/" + file.getName());
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
