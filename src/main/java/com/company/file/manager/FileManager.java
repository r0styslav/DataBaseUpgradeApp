package com.company.file.manager;

import com.company.file.parser.XmlParser;
import com.company.file.storage.DataStorage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
/**
 * Created by Rostyslav.Pash on 22-Feb-16.
 *
 * Class to copy files from srcDir to destDir
 */

//C:\!Work\copy Test\dir1
//C:\!Work\copy Test\dir2

public class FileManager {
    private String xmlName = "property.xml";
    private String xmlDefaultPath = "C:/!Work/copy Test/" + xmlName;
    private ArrayList<File> srcDir = new ArrayList<>();
    private ArrayList<File> destDir = new ArrayList<>();
    private ArrayList<File[]> filesList = new ArrayList<>();
    private DataStorage dataStorage;
    private XmlParser xmlParser;

    /**
     * Constructor to set path's set in string parameters
     * @param srcDir
     * @param destDir
     */
    public FileManager(String srcDir, String destDir) {
        this.srcDir.add(new File(srcDir));
        this.destDir.add(new File(destDir));
        this.filesList = getListOfFiles();
    }

    /**
     * Constructor to set path's from xml
     */
    public FileManager() {
        xmlParser = new XmlParser();
        File file = new File(getProgramCurrentDirectory() + "\\" + xmlName);

        try {
            xmlParser.parseSax(file.exists() ? file.getPath() : xmlDefaultPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        dataStorage = xmlParser.getDataStorage();
        for (int i = 0; i < dataStorage.getSourcePath().size(); i++) {
                srcDir.add(new File(dataStorage.getSourcePath().get(i)));
                destDir.add(new File(dataStorage.getDestPath().get(i)));
        }
        this.filesList = getListOfFiles();
        System.out.println(getProgramCurrentDirectory());
    }

    /**
     * Method that returns current working directory
     * @return String
     */

    public String getProgramCurrentDirectory() {
        return Paths.get(".").toAbsolutePath().normalize().toString();
    }

    /**
     * Method that runs cmd file in current dir
     */
    public void runScript() {
        System.out.println("Running bat files");
        if (dataStorage.getScriptsToExecute().size() != 0)
        for (String scriptPath:
            dataStorage.getScriptsToExecute()) {
            try {
                //Runtime.getRuntime().exec("cmd /c \"C:\\!Work\\copy Test\\script.bat\""); //leave for Jenkins
                //Runtime.getRuntime().exec("cmd /c start C:\\script.bat"); //leave for Jenkins
                Process proc = Runtime.getRuntime().exec("cmd.exe /c start " +  scriptPath);
                System.out.println("Script: " + scriptPath + " executed!");
            } catch (IOException io) {
                System.out.println("Bat file was not executed:");
                System.out.println(io.getStackTrace());
            }
        }

        System.out.println("Running bat files finished");
    }

    /**
     * Method prints files in scrDir
     */
    public void printListOfFiles() {
        for (int i = 0; i < filesList.size(); i++) {
            for (File file : filesList.get(i)) {
                if (file.isFile()) {
                    System.out.println("File: " + file.getName());
                }
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
            System.out.println("File All.sql created");
        } else {
            System.out.println("File All.sql already exist");
        }

    }

    public void createAllSqlFile() throws IOException {
        for (File targetDir :
                destDir) {
            File file = new File(targetDir.toString() + "\\All.sql");
            if (file.createNewFile()) {
                System.out.println("File All.sql created in " + targetDir.getPath() + "\\");
            } else {
                System.out.println("File All.sql already exist " + targetDir.getPath() + "\\");
            }
            // Add all filenames to All.sql
            //FileUtils.writeStringToFile(file, "String to append", true);
            try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(targetDir.toString() + "\\All.sql", false)))) {
                for (File dir :
                        targetDir.listFiles()) {
                        if (!dir.getName().equalsIgnoreCase("All.sql"))
                            out.println(dir.getName());
                }
            }catch (IOException e) {
                System.err.println(e);
            }


        }

    }

    /**
     * Copy all files from srcDir to destDir set in parameters
     */
    public void copyAllFiles() {
        for (int i = 0; i < filesList.size(); i++) {
            for (File file : filesList.get(i)) {
                try {
                    InputStream in = new FileInputStream(file);
                    if (!destDir.get(i).isDirectory() || !destDir.get(i).exists()){
                        Path pathToFile = Paths.get(destDir.get(i).toString());
                        Files.createDirectories(pathToFile);
                    }
                    OutputStream out = new FileOutputStream(destDir.get(i) + "/" + file.getName());
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();
                    System.out.println(file.getName() + " file copied successfully to " + destDir.get(i).getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
