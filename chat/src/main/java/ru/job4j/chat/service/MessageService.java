package ru.job4j.chat.service;

import org.springframework.stereotype.Service;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.repository.MessageRepository;
import ru.job4j.chat.repository.PersonRepository;
import ru.job4j.chat.repository.RoomRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private final MessageRepository messages;
    private final PersonRepository persons;
    private final RoomRepository rooms;

    public MessageService(final MessageRepository messages, final PersonRepository persons, final RoomRepository rooms) {
        this.messages = messages;
        this.persons = persons;
        this.rooms = rooms;
    }

    public List<Message> findAll() {
        return this.messages.findAll();
    }

    public Optional<Message> findById(int id) {
        return this.messages.findById(id);
    }

    public Message save(Message message) {
        return this.messages.save(message);
    }

    public boolean delete(Message message) {
        return this.messages.deleteMessageById(message.getId()) != 0;
    }

    public Optional<List<Message>> findByPersonId(int id) {
        Optional<Person> personOptional = persons.findById(id);
        return personOptional.isPresent() ? Optional.of(personOptional.get().getMessages()) : Optional.empty();
    }

    public Optional<List<Message>> findByRoomId(int id) {
        Optional<Room> roomOptional = rooms.findById(id);
        return roomOptional.isPresent() ? Optional.of(roomOptional.get().getMessages()) : Optional.empty();
    }
}
