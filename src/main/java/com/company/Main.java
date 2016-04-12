package com.company;

import com.company.file.manager.FileManager;

public class Main {

    public static void main(String[] args) throws Exception {
	    // write your code here

        FileManager fileManager = new FileManager();
        fileManager.copyAllFiles();
        fileManager.createAllSqlFile();
        fileManager.runScript();
    }
}
