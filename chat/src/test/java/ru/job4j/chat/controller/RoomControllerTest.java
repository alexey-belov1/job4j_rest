package ru.job4j.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.chat.ChatApp;
import ru.job4j.chat.model.Room;
import ru.job4j.chat.service.MessageService;
import ru.job4j.chat.service.RoomService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ChatApp.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class RoomControllerTest {

    @Autowired
    private RoomService roomService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenFindAll() throws Exception {
        this.mockMvc.perform(get("/room/"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        new ObjectMapper().writeValueAsString(
                                List.of(InitDataDB.room1, InitDataDB.room2))
                        )
                );
    }

    @Test
    public void whenFindByIdAndFound() throws Exception {
        this.mockMvc.perform(get("/room/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        new ObjectMapper().writeValueAsString(
                                InitDataDB.room1)
                        )
                );
    }

    @Test
    public void whenFindByIdAndNotFound() throws Exception {
        this.mockMvc.perform(get("/room/3"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(
                        new ObjectMapper().writeValueAsString(
                                new Room())
                        )
                );
    }

    @Test
    public void whenFindPersonsInRoomAndFound() throws Exception {
        this.mockMvc.perform(get("/room/1/person"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        new ObjectMapper().writeValueAsString(
                                List.of(InitDataDB.person1, InitDataDB.person2))
                        )
                );
    }

    @Test
    public void whenFindPersonsInRoomAndNotFound() throws Exception {
        this.mockMvc.perform(get("/room/3/person"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(
                        new ObjectMapper().writeValueAsString(
                                Collections.EMPTY_LIST)
                        )
                );
    }

    @Test
    @Transactional
    public void whenAddPersonAndOk() throws Exception {
        this.mockMvc.perform(put("/room/2/addPerson/1"))
                .andExpect(status().isOk());
        assertThat(new ArrayList<>(roomService.findById(2).get().getPersons()), is(List.of(InitDataDB.person1)));
    }

    @Test
    @Transactional
    public void whenAddPersonAndNotFoundRoom() throws Exception {
        this.mockMvc.perform(put("/room/3/addPerson/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void whenAddPersonAndNotFoundPerson() throws Exception {
        this.mockMvc.perform(put("/room/1/addPerson/3"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void whenRemovePerson() throws Exception {
        this.mockMvc.perform(delete("/room/1/removePerson/1"))
                .andExpect(status().isOk());
        assertThat(new ArrayList<>(roomService.findById(1).get().getPersons()), is(List.of(InitDataDB.person2)));
    }

    @Test
    @Transactional
    public void whenRemovePersonAndNotFoundRoom() throws Exception {
        this.mockMvc.perform(delete("/room/3/removePerson/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void whenRemovePersonAndNotFoundPerson() throws Exception {
        this.mockMvc.perform(delete("/room/1/removePerson/3"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void whenCreate() throws Exception {
        Room room3 = new Room();
        room3.setId(3);
        room3.setName("room2");
        roomService.save(room3);

        String json = new ObjectMapper().writeValueAsString(room3);
        this.mockMvc.perform(
                post("/room/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertThat(roomService.findAll(), is(List.of(InitDataDB.room1, InitDataDB.room2, room3)));
    }

    @Test
    @Transactional
    public void whenUpdateAndOk() throws Exception {
        Room room = roomService.findById(1).get();
        room.setName("updatedRoom");
        String json = new ObjectMapper().writeValueAsString(room);

        this.mockMvc.perform(
                put("/room/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertThat(roomService.findById(1).get().getName(), is(room.getName()));
    }

    @Test
    @Transactional
    public void whenUpdateAndNotFound() throws Exception {
        Room room = roomService.findById(1).get();
        room.setId(3);
        String json = new ObjectMapper().writeValueAsString(room);

        this.mockMvc.perform(
                put("/room/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void whenDeleteAndOk() throws Exception {
        this.mockMvc.perform(delete("/room/1"))
                .andExpect(status().isOk());
        assertThat(roomService.findAll(), is(List.of(InitDataDB.room2)));
        assertThat(messageService.findAll(), is(List.of(InitDataDB.message3)));
    }

    @Test
    @Transactional
    public void whenDeleteAndNotFound() throws Exception {
        this.mockMvc.perform(delete("/room/3"))
                .andExpect(status().isNotFound());
    }
}
