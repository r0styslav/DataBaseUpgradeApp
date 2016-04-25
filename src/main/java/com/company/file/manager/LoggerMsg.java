package com.company.file.manager;

import org.apache.log4j.Logger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Rostyslav.Pash on 13-Apr-16.
 */
public class LoggerMsg {
    //private String currentLocation = FileManager.getProgramCurrentDirectory();

    public static void log(String msg) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Logger logger = Logger.getLogger(new Exception().getStackTrace()[1].getClassName());
        logger.info(dateFormat.format(date) + ": " + msg);
    }

}
