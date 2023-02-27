package com.kntest.knbackend.util;


import com.kntest.knbackend.exception.UserAlreadyExistsException;
import com.kntest.knbackend.model.entity.Person;
import com.kntest.knbackend.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PersonAuthService implements UserDetailsService {

    private PersonRepository personRepository;

    public PersonAuthService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

        Person personFromRepo = personRepository.findByUsername(username).get();
        List<GrantedAuthority> authorities = personFromRepo.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(personFromRepo.getUsername(), personFromRepo.getPassword(), authorities);

    }

    public Person saveUser(Person person) throws UserAlreadyExistsException {

        if (personRepository.findByUsername(person.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("USER_EXISTS");
        }
        return personRepository.save(person);
    }
}