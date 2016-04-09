package com.company.file.parser;

import javax.xml.bind.annotation.*;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by Rostyslav.Pash on 26-Feb-16.
 */
@XmlRootElement(name="application")
@XmlAccessorType(XmlAccessType.FIELD)
public class Application {
    private String app;

    private List<Path> sourcePath;

    public void setApp(String app) {
        this.app = app;
    }

    public List<Path> getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(List<Path> sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getApp() {
        return app;
    }

    @Override
    public String toString() {
        return "Application{" +
                "app='" + app + '\'' +
                ", sourcePath=" + sourcePath +
                '}';
    }
}
