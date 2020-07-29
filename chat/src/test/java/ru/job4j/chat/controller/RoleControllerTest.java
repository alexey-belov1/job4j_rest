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
import ru.job4j.chat.model.Role;
import ru.job4j.chat.service.RoleService;

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
public class RoleControllerTest {
    @Autowired
    private RoleService roleService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void whenFindAll() throws Exception {
        this.mockMvc.perform(get("/role/"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        new ObjectMapper().writeValueAsString(
                                List.of(InitDataDB.role1, InitDataDB.role2))
                        )
                );
    }

    @Test
    public void whenFindByIdAndFound() throws Exception {
        this.mockMvc.perform(get("/role/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        new ObjectMapper().writeValueAsString(
                                InitDataDB.role1)
                        )
                );
    }

    @Test
    public void whenFindByIdAndNotFound() throws Exception {
        this.mockMvc.perform(get("/role/3"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(
                        new ObjectMapper().writeValueAsString(
                                new Role())
                        )
                );
    }

    @Test
    @Transactional
    public void whenCreate() throws Exception {
        Role role3 = new Role();
        role3.setId(3);
        role3.setName("guest");
        roleService.save(role3);

        String json = new ObjectMapper().writeValueAsString(role3);
        this.mockMvc.perform(
                post("/role/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        assertThat(roleService.findAll(), is(List.of(InitDataDB.role1, InitDataDB.role2, role3)));
    }

    @Test
    @Transactional
    public void whenUpdateAndOk() throws Exception {
        Role role = roleService.findById(1).get();
        role.setName("guest");
        String json = new ObjectMapper().writeValueAsString(role);

        this.mockMvc.perform(
                put("/role/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertThat(roleService.findById(1).get().getName(), is(role.getName()));
    }

    @Test
    @Transactional
    public void whenUpdateAndNotFound() throws Exception {
        Role role = roleService.findById(1).get();
        role.setId(3);
        String json = new ObjectMapper().writeValueAsString(role);

        this.mockMvc.perform(
                put("/role/")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void whenDeleteAndOk() throws Exception {
        this.mockMvc.perform(delete("/role/1"))
                .andExpect(status().isOk());
        assertThat(roleService.findAll(), is(List.of(InitDataDB.role2)));
    }

    @Test
    @Transactional
    public void whenDeleteAndNotFound() throws Exception {
        this.mockMvc.perform(delete("/role/3"))
                .andExpect(status().isNotFound());
    }
}
