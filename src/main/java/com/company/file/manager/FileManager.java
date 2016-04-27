package com.company.file.manager;

import com.company.file.parser.XmlParser;
import org.apache.log4j.Level;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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
        this.filesList = getListOfFiles();
    }

    /**
     * Constructor to set path's from xml
     */
    public FileManager() {
        Message.setLoggerPath(".");
        xmlParser = new XmlParser();
        File file = new File(getProgramCurrentDirectory() + xmlName);

        try {
            xmlParser.parseSax(file.exists() ? file.getPath() : xmlDefaultPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dataStorage = xmlParser.getDataStorage();
        for (int i = 0; i < dataStorage.getSourcePath().size(); i++) {
            srcDir.add(new File(dataStorage.getSourcePath().get(i)));
            targetDir.add(new File(dataStorage.getDestPath().get(i)));
        }
        this.filesList = getListOfFiles();
        Message.log(getProgramCurrentDirectory());
    }

    public void jobDO() {
        copyAllFiles();
        createAllSqlFile();
        runScript();
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
            Message.log("**************** No updates in *.sql files, scripts were not executed! ***");
    }

    /**
     * Method prints files in scrDir
     */
    public void printListOfFiles() {
        for (int i = 0; i < filesList.size(); i++) {
            for (File file : filesList.get(i)) {
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
     * @return lists of Files in scrDir's
     */
    public ArrayList<File[]> getListOfFiles() {
        for (File dir : srcDir) {
            filesList.add(dir.listFiles());
        }
        return filesList;
    }

    /**
     * Method creates new file
     */
    public void createAllSqlFile(String targetPath) throws IOException {
        File file = new File(targetPath + "\\All.sql");
        if (file.createNewFile()) {
            Message.log("File All.sql created");
        } else {
            Message.log("File All.sql already exist");
        }

    }

    public void createAllSqlFile() {
        try {
            for (File targetDir :
                    this.targetDir) {
                File allSqlFile = new File(targetDir.toString() + "\\All.sql");
                if (allSqlFile.createNewFile()) {
                    Message.log("File All.sql created in " + targetDir.getPath() + "\\");
                } else {
                    Message.log("File All.sql already exist " + targetDir.getPath() + "\\");
                }
                // Add all filenames to All.sql
                //FileUtils.writeStringToFile(file, "String to append", true);
                try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(targetDir.toString() + "\\All.sql", false)))) {
                    for (File file :
                            targetDir.listFiles()) {
                        if (file.getName().endsWith(".sql") && !file.getName().equalsIgnoreCase("All.sql"))
                            out.println("START " + file.getAbsolutePath());
                    }
                    Message.log(targetDir.toString() + "\\All.sql was updated with all script files" );
                } catch (IOException e) {
                    Message.log(e.toString(), Level.ERROR);
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
        for (int i = 0; i < filesList.size(); i++) {
            for (File file : filesList.get(i)) {
                try {
                    if (!file.getName().equalsIgnoreCase("all.sql") && !file.isDirectory() && file.getName().endsWith(".sql")) {
                        if (!targetDir.get(i).exists() || isFileChanged(file.getPath(), targetDir.get(i) + "/" + file.getName())) {
                            if (!targetDir.get(i).isDirectory() || !targetDir.get(i).exists()) {
                                Path targetPathToFile = Paths.get(targetDir.get(i).toString());
                                Files.createDirectories(targetPathToFile);
                            }
                            InputStream in = new FileInputStream(file);
                            OutputStream out = new FileOutputStream(targetDir.get(i) + "/" + file.getName());
                            byte[] buf = new byte[1024];
                            int len;
                            while ((len = in.read(buf)) > 0) {
                                out.write(buf, 0, len);
                            }
                            in.close();
                            out.close();
                            areFilesDifferent = true;
                            Message.log(file.getName() + " file copied successfully to " + targetDir.get(i).getPath());
                        } else
                            Message.log(file.getName() + " file NOT copied to " + targetDir.get(i).getPath());
                    } else
                        Message.log(file.getName() + " file NOT copied to " + targetDir.get(i).getPath() + " [UP TO DATE]");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        clearAllRemovedFiles();
    }
    /**
     * Method should remove all files from targetDir that do not exist in sourceDir
     */
    private void clearAllRemovedFiles() {
        
    }

    /**
     * Check if some changes were made in source file
     */
    public boolean isFileChanged(String sourceFilePath, String targetFilePath) {
        File sourceFile = new File(sourceFilePath);
        File targetFile = new File(targetFilePath);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        if (sourceFile.lastModified() > targetFile.lastModified()) {
            Message.log("[NEW version] " + sourceFilePath + " modified: " + sdf.format(sourceFile.lastModified()));
            Message.log("[OLD version] " + targetFilePath + " modified: " + sdf.format(targetFile.lastModified()));
            return true;
        } else {
            return false;
        }
    }
}
