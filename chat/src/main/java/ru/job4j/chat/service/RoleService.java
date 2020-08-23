package ru.job4j.chat.service;

import org.springframework.stereotype.Service;
import ru.job4j.chat.model.Role;
import ru.job4j.chat.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roles;

    public RoleService(final RoleRepository roles) {
        this.roles = roles;
    }

    public List<Role> findAll() {
        return this.roles.findAll();
    }

    public Optional<Role> findById(int id) {
        return this.roles.findById(id);
    }

    public Role save(Role role) {
        return this.roles.save(role);
    }

    public boolean update(Role role) {
        Optional<Role> optional = this.roles.findById(role.getId());
        if (optional.isPresent()) {
            this.roles.save(role);
            return true;
        }
        return false;
    }

    public boolean delete(Role role) {
        return this.roles.deleteRoleById(role.getId()) != 0;
    }
}
