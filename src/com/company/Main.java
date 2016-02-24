package com.company;

import com.company.file.manager.FileTracer;
import com.company.file.parser.XmlParser;

public class Main {

    public static String srcDirPath = "";
    public static String destDirPath = "";
    public static String xmlPath = "C:/!Work/copy Test/property.xml";

    public static void main(String[] args) throws Exception {
	    // write your code here
/*        XmlParser xmlParser = new XmlParser();
        xmlParser.parseDocument(xmlPath);
        FileTracer fileTracer = new FileTracer(srcDirPath, destDirPath);
        fileTracer.printListOfFiles();
        fileTracer.copyAllFiles();
        fileTracer.printListOfFiles(destDirPath);*/

        FileTracer fileTracer = new FileTracer();
    }
}
