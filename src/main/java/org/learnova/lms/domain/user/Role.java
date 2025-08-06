package org.learnova.lms.domain.user;
import org.learnova.lms.domain.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
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
