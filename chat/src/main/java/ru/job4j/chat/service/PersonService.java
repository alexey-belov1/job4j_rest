package ru.job4j.chat.service;

import org.springframework.stereotype.Service;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.repository.PersonRepository;

@Service
public class PersonService extends AbstractService<Person> {

    private final PersonRepository persons;

    public PersonService(final PersonRepository persons) {
        super(persons);
        this.persons = persons;
    }
}
