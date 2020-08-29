package ru.job4j.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.chat.ChatApp;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.service.PersonService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ChatApp.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
public class PersonControllerTest {

    @Autowired
    private PersonService personService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenFindAll() throws Exception {
        this.mockMvc.perform(get("/person/"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        new ObjectMapper().writeValueAsString(
                                List.of(InitDataDB.person1, InitDataDB.person2))
                        )
                );
    }

    @Test
    public void whenFindByIdAndFound() throws Exception {
        this.mockMvc.perform(get("/person/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        new ObjectMapper().writeValueAsString(
                                InitDataDB.person1)
                        )
                );
    }

    @Test
    public void whenFindByIdAndNotFound() throws Exception {
        this.mockMvc.perform(get("/person/3"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(
                        new ObjectMapper().writeValueAsString(
                                new Person())
                        )
                );
    }

    @Test
    @Transactional
    @Rollback(false)
    public void whenCreate() throws Exception {
        Person person3 = new Person();
        person3.setId(3);
        person3.setName("person3");
        person3.setPassword("111");

        String json = new ObjectMapper().writeValueAsString(person3);
        this.mockMvc.perform(
                post("/person/sign-up")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertThat(new ArrayList<>(personService.findAll()), is(List.of(InitDataDB.person1, InitDataDB.person2, person3)));
        personService.delete(person3);
    }

    @Test
    @Transactional
    public void whenUpdateAndOk() throws Exception {
        Person person = personService.findById(1).get();
        person.setName("updatedPerson");
        String json = new ObjectMapper().writeValueAsString(person);

        this.mockMvc.perform(
                    put("/person/")
                            .content(json)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertThat(personService.findById(1).get().getName(), is(person.getName()));
    }

    @Test
    @Transactional
    public void whenUpdateAndNotFound() throws Exception {
        Person person = personService.findById(1).get();
        person.setId(3);
        String json = new ObjectMapper().writeValueAsString(person);

        this.mockMvc.perform(
                put("/person/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void whenDeleteAndOk() throws Exception {
        this.mockMvc.perform(delete("/person/1"))
                .andExpect(status().isOk());
        assertThat(personService.findAll(), is(List.of(InitDataDB.person2)));
    }

    @Test
    @Transactional
    public void whenDeleteAndNotFound() throws Exception {
        this.mockMvc.perform(delete("/person/3"))
                .andExpect(status().isNotFound());
    }
}
