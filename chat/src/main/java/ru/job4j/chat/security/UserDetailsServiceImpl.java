package ru.job4j.chat.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.job4j.chat.model.Person;
import ru.job4j.chat.service.PersonService;

import java.util.Collection;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private PersonService personService;

    public UserDetailsServiceImpl(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        List<Person> persons = personService.findByName(name);
        if (persons.isEmpty()) {
            throw new UsernameNotFoundException(name);
        }
        Person person = persons.get(0);
        Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(person.getRole().getName()));
        return new User(person.getName(), person.getPassword(), authorities);
    }
}