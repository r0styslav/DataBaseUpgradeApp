package com.company.file.parser;

import com.company.file.storage.DataStorage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;

/**
 * Created by Rostyslav.Pash on 23-Feb-16.
 */
public class XmlParser {
    private DataStorage dataStorage;

    public void parseSax(String xmlFileSrc) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        dataStorage = new DataStorage();
        ParserSax parser = new ParserSax(dataStorage);

        saxParser.parse(new File(xmlFileSrc), parser);
        //dataStorage = parser.getDataStorage();
    }

    public  Ompartners parseJaxb(String xmlFileSrc) throws JAXBException {

        File file = new File(xmlFileSrc);
        JAXBContext jaxbContext = JAXBContext.newInstance(Ompartners.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        Ompartners ompartners = (Ompartners) jaxbUnmarshaller.unmarshal(file);
        System.out.println(ompartners);
        return ompartners;

    }
    public DataStorage getDataStorage() {
        return dataStorage;
    }
}
