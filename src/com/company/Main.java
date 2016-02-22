package com.company;

import com.company.file.manager.FileTracer;

public class Main {

    public static String srcDirPath = "C:/!Work/copy Test/dir1";
    public static String destDirPath = "C:/!Work/copy Test/dir2";

    public static void main(String[] args) {
	// write your code here
        FileTracer fileTracer = new FileTracer(srcDirPath, destDirPath);
        fileTracer.printListOfFiles();
        fileTracer.copyAllFiles();
        fileTracer.printListOfFiles(destDirPath);
    }
}
