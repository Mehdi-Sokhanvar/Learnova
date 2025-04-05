package org.learnova.lms.domain.question;


import jakarta.persistence.Entity;
import org.learnova.lms.domain.base.BaseEntity;

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


