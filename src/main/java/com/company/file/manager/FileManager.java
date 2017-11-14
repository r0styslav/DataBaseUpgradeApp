package com.company.file.manager;

import com.company.file.parser.XmlParser;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Level;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Pattern;

/**
 * Created by Rostyslav.Pash on 22-Feb-16.
 * <p/>
 * Class to copy files from srcDir to targetDir
 */

//C:\!Work\copy Test\dir1
//C:\!Work\copy Test\dir2

public class FileManager extends BaseSettings {
    /**
     * Constructor to set path's set in string parameters
     *
     * @param srcDir
     * @param targetDir
     */
    public FileManager(String srcDir, String targetDir) {
        this.srcDir.add(new File(srcDir));
        this.targetDir.add(new File(targetDir));
        this.sourceFilesList = getListOfFiles();
    }

    /**
     * Constructor to set path's from xml
     */
    public FileManager(String propertyName) {
        Message.setLoggerPath(".");
        xmlParser = new XmlParser();
        File file = new File(getProgramCurrentDirectory() + (propertyName.isEmpty() ? xmlName : propertyName));

        try {
            Message.log("************ Parsing " + file.getName().toString() + " ..........");
            xmlParser.parseSax(file.exists() ? file.getPath() : xmlDefaultPath);
            Message.log("************ Finished parsing "  + file.getName().toString() + " ..........");
        } catch (Exception e) {
            Message.log("Property file " + file.toString() + "name or location is incorrect", Level.ERROR);
        }
        dataStorage = xmlParser.getDataStorage();
        for (int i = 0; i < dataStorage.getSourcePath().size(); i++) {
            srcDir.add(new File(dataStorage.getSourcePath().get(i)));
            targetDir.add(new File(dataStorage.getDestPath().get(i)));
        }
        this.sourceFilesList = getListOfFiles();
        Message.log(getProgramCurrentDirectory());
    }


    /**
     * Method that runs cmd file in current dir
     */
    public void runScript() {
        if (areFilesDifferent) {
            Message.log("****************** Changes in files were made, running bat files *********");
            if (dataStorage.getScriptsToExecute().size() != 0)
                for (String scriptPath :
                        dataStorage.getScriptsToExecute()) {
                    try {
                        // Jenkins
                        //Runtime.getRuntime().exec("cmd.exe /c " + getProgramCurrentDirectory() + scriptPath);
                        Runtime.getRuntime().exec("cmd.exe /c start " + scriptPath);
                        Message.log("Script: " + scriptPath + " executed!");
                    } catch (IOException io) {
                        Message.log("Bat file was not executed:");
                        Message.log(io.getStackTrace().toString(), Level.ERROR);
                    }
                }
            Message.log("**************** Running bat files finished ******************************");
        } else
            Message.log("**************** No updates in *.sql/tql files, scripts were not executed! ***");
    }

    /**
     * Method prints files in scrDir
     */
    public void printListOfFiles() {
        for (int i = 0; i < sourceFilesList.size(); i++) {
            for (File file : sourceFilesList.get(i)) {
                if (file.isFile()) {
                    Message.log("File: " + file.getName());
                }
            }
        }
    }

    /**
     * Methods prints files in dir set in parameter
     *
     * @param filePath
     */
    public void printListOfFiles(String filePath) {
        File fileDir = new File(filePath);
        for (File file : fileDir.listFiles()) {
            if (file.isFile()) {
                Message.log("File: " + file.getName());
            }
        }
    }

    /**
     * @return Lists of Files in scrDir's
     */
    public ArrayList<File[]> getListOfFiles() {
        for (int i = 0; i < srcDir.size(); i++) {
            if (!srcDir.get(i).exists()) {
                Message.log(srcDir.get(i).toString() + " - dir does NOT exist!!! Please have a look at property.xml configurations!", Level.ERROR);
                sourceFilesList.add(new File[0]);
            }
            else
                sourceFilesList.add(srcDir.get(i).listFiles());
        }
        return sourceFilesList;
    }


/*    public void createFileForAllScripts(String targetPath) throws IOException {
        File file = new File(targetPath + "\\All.sql");
        if (file.createNewFile()) {
            Message.log("File All.sql created");
        } else {
            Message.log("File All.sql already exist");
        }

    }*/
    /**
     * Method creates new file All.sql/tql
     */
    public void createFileForAllScripts(String extensionName) {
        try {
            for (File targetDir :
                    this.targetDir) {
                File allSqlFile = new File(targetDir.toString() + "\\All." + extensionName);
                if (!targetDir.isDirectory() || !targetDir.exists() || targetDir.listFiles().length == 0) {
                    Message.log("All." + extensionName + " was not created for " + targetDir.getPath() + " because folder is empty or doesn't exist!!!",
                            Level.WARN);
                } else {
                    if (allSqlFile.createNewFile()) {
                        Message.log("File All." + extensionName + " created in " + targetDir.getPath() + "\\");
                    } else {
                        Message.log("File All." + extensionName + " already exist " + targetDir.getPath() + "\\");
                    }
                    // separate files in different lists for sorting by modified date and by name number
                    targetSortedFilesByNumber.clear();
                    targetSortedFilesByModifiedDate.clear();

                    for (File file : targetDir.listFiles()) {
                        if (file.getName().endsWith("." + extensionName) && !file.getName().equalsIgnoreCase("all." + extensionName))
                            if (Character.isDigit(file.getName().charAt(0)))
                                targetSortedFilesByNumber.add(file);
                            else
                                targetSortedFilesByModifiedDate.add(file);
                    }
                    // put names starting from digits in correct order 1_ 2_ 3_ 11_ 12_ ...
                    Collections.sort(targetSortedFilesByModifiedDate, new Comparator<File>() {
                        public int compare(File f1, File f2) {
                            BasicFileAttributes basicFileAttributes1 = null;
                            BasicFileAttributes basicFileAttributes2 = null;
                            try {
                                for (File[] f : sourceFilesList) {
                                    if (f == null) return 0;
                                    for (int i = 0; i < f.length; i++) {
                                        if (f[i].getName().equals(f1.getName()))
                                            basicFileAttributes1 = Files.readAttributes(f[i].toPath(), BasicFileAttributes.class);
                                        if (f[i].getName().equals(f2.getName()))
                                            basicFileAttributes2 = Files.readAttributes(f[i].toPath(), BasicFileAttributes.class);
                                    }
                                }
                                if (basicFileAttributes1 == null)
                                    basicFileAttributes1 = Files.readAttributes(f1.toPath(), BasicFileAttributes.class);
                                if (basicFileAttributes2 == null)
                                    basicFileAttributes2 = Files.readAttributes(f2.toPath(), BasicFileAttributes.class);

                            } catch (IOException e) {
                                Message.log(e.getMessage(), Level.ERROR);
                            }
                            return basicFileAttributes1.lastModifiedTime().compareTo(basicFileAttributes2.lastModifiedTime());
                        }
                    });

                    Collections.sort(targetSortedFilesByNumber, new Comparator<File>() {
                        public int compare(File f1, File f2) {
                            if (Character.isDigit(f1.getName().charAt(0)) && Character.isDigit(f2.getName().charAt(0))) {
                                String pattern = "[^0-9]+";
                                Pattern p = Pattern.compile(pattern);
                                int i1 = Integer.parseInt(f1.getName().replaceFirst(pattern, " ").split(" ")[0]);
                                int i2 = Integer.parseInt(f2.getName().replaceFirst(pattern, " ").split(" ")[0]);
                                return i1 - i2;
                            } else
                                return 0;
                        }
                    });

                    // Add all sorted filenames to main script All.*
                    try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(targetDir.toString() + "\\All." + extensionName, false)))) {
                        for (File file :
                                targetSortedFilesByNumber) {
                            if (file.getName().endsWith("." + extensionName) && !file.getName().equalsIgnoreCase("All." + extensionName))
                                out.println((extensionName.equalsIgnoreCase("sql") ? "START " : ":r " )
                                        + file.getAbsolutePath());
                        }
                        for (File file :
                                targetSortedFilesByModifiedDate) {
                            if (file.getName().endsWith("." + extensionName) && !file.getName().equalsIgnoreCase("All." + extensionName))
                                out.println((extensionName.equalsIgnoreCase("sql") ? "START " : ":r ")
                                        + file.getAbsolutePath());
                        }
                        Message.log(targetDir.toString() + "\\All." + extensionName + " was updated with all script files");
                    } catch (IOException e) {
                        Message.log(e.toString(), Level.ERROR);
                    }
                }
            }
        } catch (IOException e) {
            Message.log(e.toString(), Level.ERROR);
        }
    }

    /**
     * Copy all files from srcDir to targetDir set in parameters
     */
    public void copyAllFiles() {
        for (int i = 0; i < sourceFilesList.size(); i++) {
            try {
                for (File file : sourceFilesList.get(i)) {
                    try {
                        if (!file.getName().equalsIgnoreCase("all.sql")
                                && !file.getName().equalsIgnoreCase("all.tql")
                                && !file.isDirectory()
                                && (file.getName().endsWith(".sql")
                                || file.getName().endsWith(".tql"))) {
                            if (!targetDir.get(i).exists() || isFileChanged(file.getPath(), targetDir.get(i) + "\\" + file.getName())) {
                                if (!targetDir.get(i).isDirectory() || !targetDir.get(i).exists()) {
                                    Path targetPathToFile = Paths.get(targetDir.get(i).toString());
                                    Files.createDirectories(targetPathToFile);
                                }
                                InputStream in = new FileInputStream(file);
                                OutputStream out = new FileOutputStream(targetDir.get(i) + "\\" + file.getName());
                                byte[] buf = new byte[1024];
                                int len;
                                while ((len = in.read(buf)) > 0) {
                                    out.write(buf, 0, len);
                                }
                                in.close();
                                out.close();
                                areFilesDifferent = true;
                                Message.log(file.getName() + " file copied successfully to " + targetDir.get(i).getPath() + " [COPIED]");
                            } else
                                Message.log(file.getName() + " file NOT copied to " + targetDir.get(i).getPath() + " [UP TO DATE]");
                        } else
                            Message.log(file.getName() + " file NOT copied to " + targetDir.get(i).getPath() + " [WRONG TYPE]");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (NullPointerException np) {
                Message.log(srcDir.get(i).getPath() + " does not exist ! Files were not copied to " + targetDir.get(i).getPath(),
                        Level.WARN);
            }
        }
        //clearAllRemovedFiles();
    }

    /**
     * Method should remove all files from targetDir that do not exist in sourceDir
     */
    private void clearAllRemovedFiles() {
        for (int i = 0; i < targetDir.size(); i++) {
            for (File targetFile :
                    targetDir.get(i).listFiles()) {
                File sourceFile = new File(srcDir.get(i).getPath() + "\\" + targetFile.getName());
                if (!targetFile.getName().equalsIgnoreCase("all.sql") && !sourceFile.exists()) {
                    if (targetFile.delete()) {
                        Message.log(sourceFile.getAbsolutePath() + " does not exist");
                        Message.log("File " + targetFile.getName() + " was removed from " + targetDir.get(i) + " [DELETED]");
                    } else {
                        Message.log(sourceFile.getAbsolutePath() + " does not exist");
                        Message.log("File " + targetFile.getName() + " was not removed from " + targetDir.get(i) + " [NOT DELETED]");
                    }
                }
            }
        }
    }

    /**
     * Check if some changes were made in source file
     */
    public boolean isFileChanged(String sourceFilePath, String targetFilePath) {
        File sourceFile = new File(sourceFilePath);
        File targetFile = new File(targetFilePath);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        if (!targetFile.exists()) {
            Message.log("[NEW version] " + sourceFilePath + " modified: " + sdf.format(sourceFile.lastModified()));
            return true;
        } else if (sourceFile.lastModified() > targetFile.lastModified()) {
            Message.log("[NEW version] " + sourceFilePath + " modified: " + sdf.format(sourceFile.lastModified()));
            Message.log("[OLD version] " + targetFilePath + " modified: " + sdf.format(targetFile.lastModified()));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Method that returns file extension
     */
    public String getFileExtension(File file) {
        return FilenameUtils.getExtension(file.getName());
    }
}
