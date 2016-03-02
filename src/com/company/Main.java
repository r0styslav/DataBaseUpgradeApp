package com.company;

import com.company.file.manager.FileManager;

public class Main {

    public static void main(String[] args) throws Exception {
	    // write your code here
/*         XmlParser xmlParser = new XmlParser();
        xmlParser.parseSax(xmlPath);
         FileManager fileManager = new FileManager(srcDirPath, destDirPath);
        fileManager.printListOfFiles();
        fileManager.copyAllFiles();
        fileManager.printListOfFiles(destDirPath);*/

        FileManager fileManager = new FileManager();
        fileManager.copyAllFiles();
    }
}
