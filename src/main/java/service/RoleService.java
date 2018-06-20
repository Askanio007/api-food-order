package service;

import dao.RoleDao;
import entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class RoleService {

    @Autowired
    @Qualifier("roleDao")
    private RoleDao roleDao;

    @Transactional
    public Role getRole(long id) {
        return roleDao.find(id);
    }
}
