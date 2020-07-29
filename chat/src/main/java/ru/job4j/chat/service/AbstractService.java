package ru.job4j.chat.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.chat.model.IModel;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class AbstractService<T extends IModel> {

    private CrudRepository<T, Integer> repository;

    public AbstractService(CrudRepository<T, Integer> repository) {
        this.repository = repository;
    }

    public List<T> findAll() {
        return StreamSupport.stream(
                this.repository.findAll().spliterator(), false
        ).collect(Collectors.toList());
    }

    public Optional<T> findById(int id) {
        return this.repository.findById(id);
    }

    public T save(T t) {
        return this.repository.save(t);
    }

    @Transactional
    public boolean update(T t) {
        Optional<T> optional = this.repository.findById(t.getId());
        if (optional.isPresent()) {
            this.repository.save(t);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean delete(T t) {
        Optional<T> optional = this.repository.findById(t.getId());
        if (optional.isPresent()) {
            this.repository.delete(optional.get());
            return true;
        }
        return false;
    }
}
