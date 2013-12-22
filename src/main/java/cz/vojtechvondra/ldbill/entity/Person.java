package cz.vojtechvondra.ldbill.entity;


import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Person implements Entity {

    protected String personId;

    protected String firstName;

    protected String lastName;

    protected String titleBeforeName;

    protected String titleAfterName;

    protected Date dateOfBirth;

    static Logger logger = Logger.getLogger(Person.class);

    public Person() {
    }

    public Person(String[] data) {
        if (data.length != 9) {
            throw new IllegalArgumentException("Invalid person row");
        }

        personId = data[0];
        titleBeforeName = data[1];
        firstName = data[2];
        lastName = data[3];
        titleAfterName = data[4];
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        try {
            dateOfBirth = sdf.parse(data[5]);
        } catch (ParseException e) {
            dateOfBirth = null;
        }
    }

    @Override
    public String getRdfUri() {
        return "http://linked.opendata.cz/resource/psp.cz/person/" + getPersonId();
    }
    public String getPersonId() {
        return personId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getTitleBeforeName() {
        return titleBeforeName;
    }

    public String getTitleAfterName() {
        return titleAfterName;
    }


    public Date getDateOfBirth() {
        return dateOfBirth;
    }
}
