package dao;

import entity.Role;
import org.springframework.stereotype.Repository;

@Repository("roleDao")
public class RoleDAOImpl extends GenericDAOImpl<Role> implements RoleDao {
}
