package com.company.file.parser;

import com.company.file.storage.DataStorage;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;

/**
 * Created by Rostyslav.Pash on 23-Feb-16.
 */
public class XmlParser {
    private DataStorage dataStorage;

    public void parseDocument(String xmlFileSrc) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        ParserSax parser = new ParserSax();

        saxParser.parse(new File(xmlFileSrc), parser);
        dataStorage = parser.getDataStorage();
    }

    public DataStorage getDataStorage() {
        return dataStorage;
    }
}
