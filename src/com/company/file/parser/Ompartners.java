package com.company.file.parser;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by Rostyslav.Pash on 26-Feb-16.
 */
@XmlRootElement(name="ompartners")
@XmlAccessorType(XmlAccessType.FIELD)
public class Ompartners {
     List<Application> application;

    public List<Application> getApplication() {
        return application;
    }

    public void setApplication(List<Application> application) {
        this.application = application;
    }

    @Override
    public String toString() {
        return "Ompartners{" +
                "application=" + application +
                '}';
    }
}
