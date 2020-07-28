package ru.job4j.rest.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.job4j.auth.domain.Person;

import java.util.List;

@Service
public class RestTemplateService {

    private static final String API = "http://localhost:8080/person/";
    private static final String API_ID = "http://localhost:8080/person/{id}";

    public List<Person> findAll() {
        RestTemplate rest = new RestTemplate();
        return rest
                .exchange(API,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Person>>() { })
                .getBody();
    }

    public Person findById(int id) {
        RestTemplate rest = new RestTemplate();
        return rest.getForObject(API_ID, Person.class, id);
    }

    public Person create(Person person) {
        RestTemplate rest = new RestTemplate();
        return rest.postForObject(API, person, Person.class);
    }

    public void update(Person person) {
        RestTemplate rest = new RestTemplate();
        rest.put(API, person);
    }

    public void delete(int id) {
        RestTemplate rest = new RestTemplate();
        rest.delete(API_ID, id);
    }
}
