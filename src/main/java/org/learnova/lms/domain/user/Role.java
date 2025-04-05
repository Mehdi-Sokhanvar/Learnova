package org.learnova.lms.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import org.learnova.lms.domain.base.BaseEntity;

import java.util.List;

@Entity
public class Role extends BaseEntity<Long> {

    @NotBlank
    @Column(nullable = false)
    private String name;

    private String description;

    @OneToMany(mappedBy = "role")
    private List<AppUser> userList;

    public Role() {

    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<AppUser> getUserList() {
        return userList;
    }


    public Role(String name) {
        this.name = name;
    }
}
