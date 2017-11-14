package com.company.file.manager;

import com.company.file.parser.XmlParser;
import com.company.file.storage.DataStorage;

import java.io.File;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.*;

/**
 * Created by Rostyslav.Pash on 26-Apr-16.
 */
public class BaseSettings {
    protected String xmlName = "property.xml";
    protected String xmlDefaultPath = "C:/!Work/copyTest/Upgrade/" + xmlName;
    protected ArrayList<File> srcDir = new ArrayList<>();
    protected ArrayList<File> targetDir = new ArrayList<>();
    protected ArrayList<File[]> sourceFilesList = new ArrayList<>();
    protected DataStorage dataStorage;
    protected XmlParser xmlParser;
    protected boolean areFilesDifferent = false;
    protected List<File> targetSortedFilesByNumber = new ArrayList<>();
    protected List<File> targetSortedFilesByModifiedDate = new ArrayList<>();
    protected Map<File, FileTime> filesTimeMap = new HashMap<>();
    protected ArrayList<File[]> sourceSortedFilesList = new ArrayList<>();

    /**
     * Method that returns current working directory
     *
     * @return String
     */

    public static String getProgramCurrentDirectory() {
        return Paths.get(".").toAbsolutePath().normalize().toString() + "\\";
    }
}
