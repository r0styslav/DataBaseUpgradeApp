package com.company.file.parser;

import com.company.file.manager.Message;
import com.company.file.storage.DataStorage;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * Created by Rostyslav.Pash on 23-Feb-16.
 */
public class ParserSax extends DefaultHandler{
    ArrayList<String> elementAttributes = new ArrayList<>();
    String applicationType = "";

    public DataStorage getDataStorage() {
        return dataStorage;
    }

    public ParserSax(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    DataStorage dataStorage;

    /**
     * Start of document parsing
     * @throws SAXException
     */
    @Override
    public void startDocument() throws SAXException {
    }

    /**
     * Goes through each xml element one by one
     * @param uri
     * @param localName
     * @param qName
     * @param attributes
     * @throws SAXException
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //System.out.println("Qname: " + qName);
        //System.out.println("Attributes: " + attributes.getValue(0) );
        dataStorage.setAttributes(attributes);
        dataStorage.setElement(qName);
        //applicationType = attributes.getValue(0);
    }

    /**
     * Reads characters of current element
     * @param ch
     * @param start
     * @param length
     * @throws SAXException
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String s = new String(ch, start, length);
        if (s.charAt(s.length() - 1) != '\\' || s.charAt(s.length() - 1) != '/')
            s += "\\";

        switch (dataStorage.getElement()) {
            case "application":
                applicationType = dataStorage.getAttributes().getValue(0);
                dataStorage.setAttributeValue(dataStorage.getAttributes().getValue(0));
                break;
            case "source":
                dataStorage.setSourcePath(s);
                break;
            case "target":
                dataStorage.setDestPath(s + applicationType);
                break;
            case "script":
                dataStorage.setScriptsToExecute(s);
                break;
        }
    }

    /**
     * Reading of element is finished
     * @param uri
     * @param localName
     * @param qName
     * @throws SAXException
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        dataStorage.setElement("");
        dataStorage.setAttributes(null);
        //applicationType = "";
    }

    /**
     * End of parsing document
     * @throws SAXException
     */
    @Override
    public void endDocument() throws SAXException {
        Message.log("Attributes = " + dataStorage.getAttributeValue());
        Message.log("source: " + dataStorage.getSourcePath());
        Message.log("target: " + dataStorage.getDestPath());
        Message.log("scripts: " + dataStorage.getScriptsToExecute());
    }
}
