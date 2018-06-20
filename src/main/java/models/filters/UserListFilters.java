package models.filters;

import enums.RoleType;
import lombok.Getter;
import lombok.Setter;
import utils.PaginationFilter;

@Setter
@Getter
public class UserListFilters {

    private String login;
    private String role;
    PaginationFilter paginationFilter;

    public UserListFilters() {}

}
