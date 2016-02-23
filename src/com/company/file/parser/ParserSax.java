package com.company.file.parser;

import com.company.file.storage.DataStorage;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by Rostyslav.Pash on 23-Feb-16.
 */
public class ParserSax extends DefaultHandler{

    DataStorage dataStorage = new DataStorage();

    @Override
    public void startDocument() throws SAXException {
        System.out.println("Start parsing..........");
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        System.out.println("Qname: " + qName);
        System.out.println("Attributes: " + attributes);

        dataStorage.setElement(qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        switch (dataStorage.getElement()) {
            case "ompartners":
                System.out.println("ompartners");
                break;
            case "application":
                dataStorage.setApplication(new String(ch, start, length));
                System.out.println("application: " + dataStorage.getApplicationType());
                break;
            case "sourcepath":
                dataStorage.setSourcePath(new String(ch, start, length));
                System.out.println("sourcepath: " + dataStorage.getSourcePath());
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        dataStorage.setElement("");
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println("Finished parsing ............");
    }
}
