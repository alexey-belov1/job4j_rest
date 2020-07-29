package ru.job4j.chat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.repository.MessageRepository;
import ru.job4j.chat.repository.PersonRepository;
import ru.job4j.chat.repository.RoomRepository;

import java.util.List;
import java.util.Optional;


@Service
public class MessageService extends AbstractService<Message> {

    private final MessageRepository messages;
    private final PersonRepository persons;
    private final RoomRepository rooms;

    public MessageService(final MessageRepository messages, final PersonRepository persons, final RoomRepository rooms) {
        super(messages);
        this.messages = messages;
        this.persons = persons;
        this.rooms = rooms;
    }

    @Transactional
    public Optional<List<Message>> findByPersonId(int id) {
        Optional<Person> personOptional = persons.findById(id);
        return personOptional.isPresent() ? Optional.of(personOptional.get().getMessages()) : Optional.empty();
    }

    @Transactional
    public Optional<List<Message>> findByRoomId(int id) {
        Optional<Room> roomOptional = rooms.findById(id);
        return roomOptional.isPresent() ? Optional.of(roomOptional.get().getMessages()) : Optional.empty();
    }
}
