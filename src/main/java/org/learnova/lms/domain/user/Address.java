package org.learnova.lms.domain.user;

import javax.persistence.Embeddable;

@Embeddable
public class Address {

    private String street;
    private String city;
    private String state;
    private String zip;
    public Address() {}
}
