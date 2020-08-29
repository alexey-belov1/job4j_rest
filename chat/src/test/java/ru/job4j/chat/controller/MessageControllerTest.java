package ru.job4j.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.chat.ChatApp;
import ru.job4j.chat.model.Message;
import ru.job4j.chat.service.MessageService;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ChatApp.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
public class MessageControllerTest {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenFindAll() throws Exception {
        this.mockMvc.perform(get("/message/"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        new ObjectMapper().writeValueAsString(
                                List.of(InitDataDB.message1, InitDataDB.message2, InitDataDB.message3))
                        )
                );
    }

    @Test
    @Transactional
    public void whenCreate() throws Exception {
        Message message4 = new Message();
        message4.setId(4);
        message4.setText("text3");
        message4.setRoom(InitDataDB.room1);
        message4.setPerson(InitDataDB.person1);

        String json = new ObjectMapper().writeValueAsString(message4);
        this.mockMvc.perform(
                        post("/message/")
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated());
        assertThat(messageService.findAll(), is(List.of(InitDataDB.message1, InitDataDB.message2, InitDataDB.message3, message4)));
    }

    @Test
    public void whenFindByIdAndFound() throws Exception {
        this.mockMvc.perform(get("/message/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        new ObjectMapper().writeValueAsString(
                                InitDataDB.message1)
                        )
                );
    }

    @Test
    public void whenFindByIdAndNotFound() throws Exception {
        this.mockMvc.perform(get("/message/4"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(
                        new ObjectMapper().writeValueAsString(
                                new Message())
                        )
                );
    }

    @Test
    public void whenFindByPersonIdAndFound() throws Exception {
        this.mockMvc.perform(get("/message/findByPersonId/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        new ObjectMapper().writeValueAsString(
                                List.of(InitDataDB.message1, InitDataDB.message3))
                        )
                );
    }

    @Test
    public void whenFindByPersonIdAndNotFound() throws Exception {
        this.mockMvc.perform(get("/message/findByPersonId/3"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(
                        new ObjectMapper().writeValueAsString(
                                Collections.EMPTY_LIST)
                        )
                );
    }

    @Test
    public void whenFindByRoomIdAndFound() throws Exception {
        this.mockMvc.perform(get("/message/findByRoomId/2"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        new ObjectMapper().writeValueAsString(
                                List.of(InitDataDB.message3))
                        )
                );
    }

    @Test
    public void whenFindByRoomIdAndNotFound() throws Exception {
        this.mockMvc.perform(get("/message/findByRoomId/3"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(
                        new ObjectMapper().writeValueAsString(
                                Collections.EMPTY_LIST)
                        )
                );
    }

    @Test
    @Transactional
    public void whenDeleteAndOk() throws Exception {
        this.mockMvc.perform(delete("/message/1"))
                .andExpect(status().isOk());
        assertThat(messageService.findAll(), is(List.of(InitDataDB.message2, InitDataDB.message3)));
    }

    @Test
    @Transactional
    public void whenDeleteAndNotFound() throws Exception {
        this.mockMvc.perform(delete("/message/4"))
                .andExpect(status().isNotFound());
    }
}