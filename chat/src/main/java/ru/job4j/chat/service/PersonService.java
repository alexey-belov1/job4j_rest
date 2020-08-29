package ru.job4j.chat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository persons;

    public PersonService(final PersonRepository persons) {
        this.persons = persons;
    }

    public List<Person> findAll() {
        return this.persons.findAll();
    }

    public Optional<Person> findById(int id) {
        return this.persons.findById(id);
    }

    public List<Person> findByName(String name) {
        return this.persons.findByName(name);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
    public Person save(Person person) {
        return this.persons.findByName(person.getName()).isEmpty()
                ? this.persons.save(person) : null;
    }

    public boolean update(Person person) {
        Optional<Person> optional = this.persons.findById(person.getId());
        if (optional.isPresent()) {
            this.persons.save(person);
            return true;
        }
        return false;
    }

    public boolean delete(Person person) {
        return this.persons.deletePersonById(person.getId()) != 0;
    }
}
