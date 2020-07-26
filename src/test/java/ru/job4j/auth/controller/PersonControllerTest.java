package ru.job4j.auth.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.auth.domain.Person;
import ru.job4j.auth.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

@WebMvcTest
@RunWith(SpringRunner.class)
public class PersonControllerTest {

    @MockBean
    private PersonRepository personRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenFindAll() throws Exception {
        Person person1 = new Person();
        person1.setId(1);
        Person person2 = new Person();
        person1.setId(2);
        List<Person> persons = List.of(person1, person2);
        when(personRepository.findAll()).thenReturn(persons);
        this.mockMvc.perform(get("/person/"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        new ObjectMapper().writeValueAsString(persons))
                );
    }

    @Test
    public void whenFindByIdWithOk() throws Exception {
        Person person = new Person();
        person.setId(1);
        when(personRepository.findById(1)).thenReturn(Optional.of(person));
        this.mockMvc.perform(get("/person/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        new ObjectMapper().writeValueAsString(person))
                );
    }

    @Test
    public void whenFindByIdWithNotFound() throws Exception {
        when(personRepository.findById(1)).thenReturn(Optional.empty());
        this.mockMvc.perform(get("/person/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(
                        new ObjectMapper().writeValueAsString(new Person()))
                );
    }

    @Test
    public void whenCreate() throws Exception {
        Person person = new Person();
        person.setId(1);
        String json = new ObjectMapper().writeValueAsString(person);
        when(personRepository.save(person)).thenReturn(person);
        this.mockMvc.perform(
                    post("/person/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(json));
    }

    @Test
    public void whenUpdate() throws Exception {
        Person person = new Person();
        person.setId(1);
        String json = new ObjectMapper().writeValueAsString(person);
        this.mockMvc.perform(
                    put("/person/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);
        verify(personRepository).save(argument.capture());
        assertThat(argument.getValue().getId(), is(1));
    }

    @Test
    public void whenDelete() throws Exception {
        this.mockMvc.perform(delete("/person/1"))
                .andExpect(status().isOk());
        ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);
        verify(personRepository).delete(argument.capture());
        assertThat(argument.getValue().getId(), is(1));
    }
}