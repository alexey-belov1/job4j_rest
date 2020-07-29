package ru.job4j.chat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.repository.PersonRepository;
import ru.job4j.chat.repository.RoomRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService extends  AbstractService<Room> {

    private final RoomRepository rooms;

    private final PersonRepository persons;

    public RoomService(final RoomRepository rooms, final PersonRepository persons) {
        super(rooms);
        this.rooms = rooms;
        this.persons = persons;
    }

    @Transactional
    public boolean addPerson(int roomId, int personId) {
        Optional<Room> optionalRoom = rooms.findById(roomId);
        Optional<Person> optionalPerson = persons.findById(personId);
        if (optionalRoom.isPresent() && optionalPerson.isPresent()) {
            Room room = optionalRoom.get();
            List<Person> personList = room.getPersons();
            Person person = optionalPerson.get();
            if (!personList.contains(person)) {
                personList.add(person);
                rooms.save(room);
                return true;
            }
        }
        return false;
    }

    @Transactional
    public boolean removePerson(int roomId, int personId) {
        Optional<Room> optionalRoom = rooms.findById(roomId);
        Optional<Person> optionalPerson = persons.findById(personId);
        if (optionalRoom.isPresent() && optionalPerson.isPresent()) {
            Room room = optionalRoom.get();
            List<Person> personList = room.getPersons();
            Person person = optionalPerson.get();
            if (personList.contains(person)) {
                personList.remove(person);
                rooms.save(room);
                return true;
            }
        }
        return false;
    }
}
