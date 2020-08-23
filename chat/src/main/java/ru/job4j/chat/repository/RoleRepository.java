package ru.job4j.chat.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.chat.model.Role;

import java.util.List;

public interface RoleRepository extends CrudRepository<Role, Integer> {

    List<Role> findAll();

    Integer deleteRoleById(Integer id);
}