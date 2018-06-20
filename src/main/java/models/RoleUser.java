package models;

import enums.RoleType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleUser {

    private RoleType role;
    private String roleView;

    public RoleUser() {

    }

    public RoleUser(RoleType role) {
        this.role = role;
        this.roleView = RoleType.roleAsString(role);

    }

}
