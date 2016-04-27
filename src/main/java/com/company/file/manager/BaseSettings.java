package com.company.file.manager;

import com.company.file.parser.XmlParser;
import com.company.file.storage.DataStorage;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by Rostyslav.Pash on 26-Apr-16.
 */
public class BaseSettings {
    protected String xmlName = "property.xml";
    protected String xmlDefaultPath = "C:/!Work/copyTest/" + xmlName;
    protected ArrayList<File> srcDir = new ArrayList<>();
    protected ArrayList<File> targetDir = new ArrayList<>();
    protected ArrayList<File[]> filesList = new ArrayList<>();
    protected DataStorage dataStorage;
    protected XmlParser xmlParser;
    protected boolean areFilesDifferent = false;

    /**
     * Method that returns current working directory
     *
     * @return String
     */

    public static String getProgramCurrentDirectory() {
        return Paths.get(".").toAbsolutePath().normalize().toString() + "\\";
    }
}
