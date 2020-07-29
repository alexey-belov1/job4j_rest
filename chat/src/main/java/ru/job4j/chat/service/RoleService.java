package ru.job4j.chat.service;

import org.springframework.stereotype.Service;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Role;
import ru.job4j.chat.repository.RoleRepository;

@Service
public class RoleService extends  AbstractService<Role> {

    private final RoleRepository roles;

    public RoleService(final RoleRepository roles) {
        super(roles);
        this.roles = roles;
    }
}
