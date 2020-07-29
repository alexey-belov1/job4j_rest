package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.service.MessageService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/")
    public List<Message> findAll() {
        return this.messageService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> findById(@PathVariable int id) {
        Optional<Message> res = this.messageService.findById(id);
        return new ResponseEntity<Message>(
                res.orElseGet(Message::new),
                res.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @GetMapping("/findByPersonId/{id}")
    public ResponseEntity<List<Message>> findByPersonId(@PathVariable int id) {
        Optional<List<Message>> res = messageService.findByPersonId(id);
        return new ResponseEntity<List<Message>>(
                res.isPresent() ? res.get() : Collections.EMPTY_LIST,
                res.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @GetMapping("/findByRoomId/{id}")
    public ResponseEntity<List<Message>> findByRoomId(@PathVariable int id) {
        Optional<List<Message>> res = messageService.findByRoomId(id);
        return new ResponseEntity<List<Message>>(
                res.isPresent() ? res.get() : Collections.EMPTY_LIST,
                res.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/")
    public ResponseEntity<Message> create(@RequestBody Message message) {
        return new ResponseEntity<Message>(
                this.messageService.save(message),
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Message message = new Message();
        message.setId(id);
        return this.messageService.delete(message)
                ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
