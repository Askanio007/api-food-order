package enums;

import models.RoleUser;

import java.util.Arrays;
import java.util.List;

public enum RoleType {

    ROLE_ADMIN(1), ROLE_USER(2), ROLE_MANAGER(3), ROLE_COOK(4);

    private long id;

    RoleType(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public static RoleType getRoleType(String role) {
        switch(role) {
            case "ROLE_ADMIN":  return ROLE_ADMIN;
            case "ROLE_USER":  return ROLE_USER;
            case "ROLE_MANAGER":  return ROLE_MANAGER;
            case "ROLE_COOK":  return ROLE_COOK;
        }
        return null;
    }

    public static String roleAsString(RoleType role) {
        switch(role) {
            case ROLE_ADMIN:  return "Администратор";
            case ROLE_USER:  return "Едок";
            case ROLE_MANAGER:  return "Распорядитель";
            case ROLE_COOK:  return "Гастроном";
        }
        return null;
    }

    public static List<RoleUser> getListRoles() {
        return Arrays.asList(new RoleUser(ROLE_USER), new RoleUser(ROLE_COOK), new RoleUser(ROLE_MANAGER));
    }

}
