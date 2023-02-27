package com.kntest.knbackend.UnitTests.AuthTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import com.kntest.knbackend.controller.AuthController;
import com.kntest.knbackend.enums.PRole;
import com.kntest.knbackend.model.dto.UserDto;
import com.kntest.knbackend.model.entity.Person;
import com.kntest.knbackend.model.entity.Role;
import com.kntest.knbackend.repository.RoleRepository;
import com.kntest.knbackend.util.PersonAuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private PersonAuthService personAuthService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AuthController controller;

    @Captor
    private ArgumentCaptor<Person> personCaptor;

    @Test
    void authControllerTest_testRegisterUser_Success() throws Exception {
        // Given
        UserDto user = new UserDto();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setRole(new HashSet<>(Arrays.asList("ROLE_ALLOW_EDIT")));

        Role adminRole = new Role("a5213e12-b274-11ed-afa1-0242ac120002", PRole.ROLE_ALLOW_EDIT);
        when(roleRepository.findByName(PRole.ROLE_ALLOW_EDIT)).thenReturn(Optional.of(adminRole));
        when(personAuthService.saveUser(any(Person.class))).thenReturn(new Person());

        Person person_ = new Person();
        person_.setId("a5213e12-b274-11ed-afa1-0242ac120002");
        person_.setUsername("testuser");
        person_.setPassword("password123");

        when(modelMapper.map(any(UserDto.class), any())).thenReturn(person_);
        when(modelMapper.map(any(Person.class), any())).thenReturn(user);

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        // When
        UserDto result = controller.registerUser(user, bindingResult);

        // Then
        verify(personAuthService).saveUser(personCaptor.capture());

        Person savedPerson = personCaptor.getValue();
        assertEquals("testuser", savedPerson.getUsername());
        assertTrue(new BCryptPasswordEncoder().matches("password123", savedPerson.getPassword()));
        assertTrue(savedPerson.getRoles().contains(adminRole));
        assertEquals(1, savedPerson.getRoles().size());
        assertNotNull(result);
    }

    @Test
    void authControllerTest_testRegisterUserWithDefaultRole_successReaderRole() throws Exception {
        // Given
        UserDto user = new UserDto();
        user.setUsername("testuser");
        user.setPassword("password123");

        Role defaultRole = new Role("a5213e12-b274-11ed-afa1-0242ac120002", PRole.ROLE_DISALLOW_EDIT);
        when(roleRepository.findByName(PRole.ROLE_DISALLOW_EDIT)).thenReturn(Optional.of(defaultRole));
        when(personAuthService.saveUser(any(Person.class))).thenReturn(new Person());

        Person person_ = new Person();
        person_.setId("a5213e12-b274-11ed-afa1-0242ac120002");
        person_.setUsername("testuser");
        person_.setPassword("password123");

        when(modelMapper.map(any(UserDto.class), any())).thenReturn(person_);
        when(modelMapper.map(any(Person.class), any())).thenReturn(user);

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        // When
        UserDto result = controller.registerUser(user, bindingResult);

        // Then
        verify(personAuthService).saveUser(personCaptor.capture());

        Person savedPerson = personCaptor.getValue();
        assertEquals("testuser", savedPerson.getUsername());
        assertTrue(new BCryptPasswordEncoder().matches("password123", savedPerson.getPassword()));
        assertTrue(savedPerson.getRoles().contains(defaultRole));
        assertEquals(1, savedPerson.getRoles().size());
        assertNotNull(result);
    }

}