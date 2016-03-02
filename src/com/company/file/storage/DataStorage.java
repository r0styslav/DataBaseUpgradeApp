package com.company.file.storage;

import org.xml.sax.Attributes;

import java.util.ArrayList;

/**

 * Created by Rostyslav.Pash on 23-Feb-16.
 */
public class DataStorage {

    private String element;
    private String applicationType;
    private ArrayList<String> sourcePath = new ArrayList<>();
    private ArrayList<String> destPath = new ArrayList<>();
    private Attributes attributes;
    private ArrayList<String> attributeValue = new ArrayList<>();

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
        this.sourcePath.add(sourcePath);
    }

    public String getApplicationType() {
        return applicationType;
    }

    public ArrayList<String> getSourcePath() {
        return sourcePath;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue.add(attributeValue);
    }

    public ArrayList<String> getAttributeValue() {
        return attributeValue;
    }

    public ArrayList<String> getDestPath() {
        return destPath;
    }

    public void setDestPath(String destPath) {
        this.destPath.add(destPath);
    }
}
