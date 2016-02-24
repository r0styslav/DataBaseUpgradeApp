package com.company.file.manager;

import com.company.file.parser.XmlParser;
import com.company.file.storage.DataStorage;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Rostyslav.Pash on 22-Feb-16.
 *
 * Class to copy files from srcDir to destDir
 */

//C:\!Work\copy Test\dir1
//C:\!Work\copy Test\dir2

public class FileTracer {
    private String xmlPath = "C:/!Work/copy Test/property.xml";
    private File srcDir;
    private File destDir;
    private ArrayList<String> srcPath = new ArrayList<>();
    private ArrayList<String> destPath = new ArrayList<>();
    private File[] filesList;
    private DataStorage dataStorage;
    private XmlParser xmlParser;

    /**
     * Constructor to set path's set in string parameters
     * @param srcDir
     * @param destDir
     */
    public FileTracer(String srcDir, String destDir) {
        this.srcDir = new File(srcDir);
        this.destDir = new File(destDir);
        this.filesList = getListOfFiles();
    }

    /**
     * Constructor to set path's from xml
     */
    public FileTracer() {
        xmlParser = new XmlParser();
        try {
            xmlParser.parseDocument(xmlPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dataStorage = xmlParser.getDataStorage();
        for (int i = 0; i < dataStorage.getSourcePath().size(); i++) {
            if (i%2 == 0)
                srcPath.add(dataStorage.getSourcePath().get(i));
            else
                destPath.add(dataStorage.getSourcePath().get(i));
        }
    }

    /**
     * Method prints files in scrDir
     */
    public void printListOfFiles() {
        for (File file : filesList) {
            if (file.isFile()) {
                System.out.println("File: " + file.getName());
            }
        }
    }

    /**
     * Methods prints files in dir set in parameter
     * @param filePath
     */
    public void printListOfFiles(String filePath) {
        File fileDir = new File(filePath);
        for (File file : fileDir.listFiles()) {
            if (file.isFile()) {
                System.out.println("File: " + file.getName());
            }
        }
    }

    /**
     * @return list of Files in scrDir
     */
    public File[] getListOfFiles() {
        filesList = srcDir.listFiles();
        return filesList;
    }

    /**
     * Copy all files from scrDir to destDir
     */
    public void copyAllFiles() {
        copyAllFiles(srcDir, destDir);
    }

    /**
     * Copy all files from srcDir to destDir set in parameters
     * @param srcDir
     * @param destDir
     */
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
