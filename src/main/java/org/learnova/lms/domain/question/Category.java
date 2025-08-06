package org.learnova.lms.domain.question;



import org.learnova.lms.domain.base.BaseEntity;

import javax.persistence.Entity;

@Entity
public class Category extends BaseEntity<Long> {

    private String name;


    public Category(String name) {
        this.name = name;
    }

    public Category() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


