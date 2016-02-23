package com.company.file.storage;

/**

 * Created by Rostyslav.Pash on 23-Feb-16.
 */
public class DataStorage {

    private String element;
    private String applicationType;
    private String sourcePath;

    public void setElement(String element) {
        this.element = element;
    }

    public String getElement() {
        return element;
    }

    public void setApplication(String applicationType) {
        this.applicationType = applicationType;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public String getSourcePath() {
        return sourcePath;
    }
}
