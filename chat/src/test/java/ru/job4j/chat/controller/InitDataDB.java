package ru.job4j.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.model.Role;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.service.MessageService;
import ru.job4j.chat.service.PersonService;
import ru.job4j.chat.service.RoleService;
import ru.job4j.chat.service.RoomService;

@Component
public class InitDataDB implements ApplicationRunner {

    @Autowired
    private PersonService personService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MessageService messageService;

    public static Role role1;
    public static Role role2;

    public static Person person1;
    public static Person person2;

    public static Room room1;
    public static Room room2;

    public static Message message1;
    public static Message message2;
    public static Message message3;

    public void run(ApplicationArguments args) {
        role1 = new Role();
        role1.setId(1);
        role1.setName("user");
        roleService.save(role1);

        role2 = new Role();
        role2.setId(2);
        role2.setName("admin");
        roleService.save(role2);

        person1 = new Person();
        person1.setId(1);
        person1.setName("person1");
        person1.setPassword("111");
        person1.setRole(role1);
        personService.save(person1);

        person2 = new Person();
        person2.setId(2);
        person2.setName("person2");
        person2.setPassword("111");
        person2.setRole(role2);
        personService.save(person2);

        room1 = new Room();
        room1.setId(1);
        room1.setName("room1");
        roomService.save(room1);

        room1.addPerson(person1);
        room1.addPerson(person2);
        roomService.update(room1);

        room2 = new Room();
        room2.setId(2);
        room2.setName("room2");
        roomService.save(room2);

        message1 = new Message();
        message1.setId(1);
        message1.setText("text1");
        message1.setPerson(person1);
        message1.setRoom(room1);
        messageService.save(message1);

        message2 = new Message();
        message2.setId(2);
        message2.setText("text2");
        message2.setPerson(person2);
        message2.setRoom(room1);
        messageService.save(message2);

        message3 = new Message();
        message3.setId(3);
        message3.setText("text3");
        message3.setPerson(person1);
        message3.setRoom(room2);
        messageService.save(message3);
    }
}
