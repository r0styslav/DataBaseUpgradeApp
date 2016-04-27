package com.company.file.manager;


import org.apache.log4j.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Rostyslav.Pash on 13-Apr-16.
 */
public class Message extends BaseSettings{
    private static Date date = new Date();
    private static DateFormat dateExactFormat = new SimpleDateFormat("HHmmss.ddMMyyyy");
    private static DateFormat dateHourFormat = new SimpleDateFormat("dd.MM.yyyy");

    public static void log(String msg, Level lvl) {
        Logger logger = Logger.getLogger(new Exception().getStackTrace()[2].getClassName());
        if (lvl == Level.INFO)
            logger.info(msg);
        else if (lvl == Level.DEBUG)
            logger.debug(msg);
        else if (lvl == Level.ERROR)
            logger.error(msg);
        else if (lvl == Level.FATAL)
            logger.fatal(msg);
    }

    public static void log(String msg) {
        log(msg, Level.INFO);
    }

    public static void setLoggerPath(String path) {
        try {
            System.setProperty("logfile.path", "logs\\" + dateExactFormat.format(date) + ".log");
            log(getProgramCurrentDirectory() + "logs\\" + dateExactFormat.format(date) + ".log file created successfully");

            /*            FileInputStream in = new FileInputStream("src\\main\\resources\\log4j.properties");
            Properties props = new Properties();
            props.load(in);
            props.getProperty("log4j.appender.LOGFILE.File");
            System.out.println("++++++++++++++++++++ " + props.getProperty("log4j.appender.LOGFILE.File"));
            in.close();

            FileOutputStream out = new FileOutputStream("src\\main\\resources\\log4j.properties");
            props.setProperty("log4j.appender.LOGFILE.File", "C:\\!Work\\copyTest\\log\\log4j.log");
            props.store(out, null);
            System.out.println("++++++++++++++++++++ " + props.getProperty("log4j.appender.LOGFILE.File"));
            out.close(); */
        } catch (Exception e) {
            log(e.toString(), Level.ERROR);
        }
    }




    private static void createLogFile(String path, String msg) {
        File file = new File(path);
        try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(path.toString() + "\\" +
                dateHourFormat.format(date) + ".log", file.exists())))) {
            out.println(msg);
        }catch (IOException e) {
            System.err.println(e);
        }

    }
}
