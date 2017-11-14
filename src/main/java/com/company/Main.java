package com.company;

import com.company.file.manager.FileManager;

public class Main {

    public static void main(String[] args) throws Exception {
	    // write your code here
        FileManager fileManager = new FileManager(args.length>0 ? args[0] : ""); // args[0] is property file name
        //FileManager fileManager = new FileManager("property.xml"); // manually set the property file name
        fileManager.copyAllFiles();
        fileManager.createFileForAllScripts("sql");
        fileManager.createFileForAllScripts("tql");
        fileManager.runScript();
    }
}
