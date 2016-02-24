package com.company.file.parser;

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

    public DataStorage getDataStorage() {
        return dataStorage;
    }

    DataStorage dataStorage = new DataStorage();

    /**
     * Start of document parsing
     * @throws SAXException
     */
    @Override
    public void startDocument() throws SAXException {
        System.out.println("Start parsing..........");
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
        //System.out.println("Attributes: " + attributes.getLocalName(0) + " : " + attributes.getQName(0) + " : " + attributes.getValue(0) );
        dataStorage.setAttributes(attributes);
        dataStorage.setElement(qName);
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
        switch (dataStorage.getElement()) {
            case "application":
                dataStorage.setAttributeValue(dataStorage.getAttributes().getValue(0));
                System.out.println("Attribute " + dataStorage.getAttributes().getQName(0) + " = " + dataStorage.getAttributeValue());
                break;
            case "sourcepath":
                dataStorage.setSourcePath(new String(ch, start, length));
                System.out.println("sourcepath: " + dataStorage.getSourcePath());
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
    }

    /**
     * End of parsing document
     * @throws SAXException
     */
    @Override
    public void endDocument() throws SAXException {
        System.out.println("Finished parsing ............");
    }
}
