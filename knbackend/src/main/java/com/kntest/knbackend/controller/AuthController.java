package com.kntest.knbackend.controller;

import com.kntest.knbackend.config.jwt.JWTUtility;
import com.kntest.knbackend.enums.PRole;
import com.kntest.knbackend.exception.SignUpPasswordComplexityException;
import com.kntest.knbackend.exception.UserAlreadyExistsException;
import com.kntest.knbackend.exception.UserNotFoundException;
import com.kntest.knbackend.model.dto.UserDto;
import com.kntest.knbackend.model.dto.jwt.JwtRequest;
import com.kntest.knbackend.model.dto.jwt.JwtResponse;
import com.kntest.knbackend.model.entity.Person;
import com.kntest.knbackend.model.entity.Role;
import com.kntest.knbackend.repository.PersonRepository;
import com.kntest.knbackend.repository.RoleRepository;
import com.kntest.knbackend.util.PersonAuthService;
import io.swagger.v3.oas.annotations.Operation;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    private final JWTUtility jwtUtility;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final PersonAuthService personAuthService;
    private final RoleRepository roleRepository;
    private final PersonRepository personRepository;

    public AuthController(JWTUtility jwtUtility,
                          ModelMapper modelMapper,
                          AuthenticationManager authenticationManager,
                          PersonAuthService personAuthService,
                          PersonRepository personRepository,
                          RoleRepository roleRepository) {
        this.jwtUtility = jwtUtility;
        this.personRepository = personRepository;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
        this.personAuthService = personAuthService;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user for getting JWT Token")
    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws UserNotFoundException {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new UserNotFoundException("INVALID_CREDENTIALS");
        }

        final UserDetails userDetails
                = personAuthService.loadUserByUsername(jwtRequest.getUsername());

        final String token =
                jwtUtility.generateToken(userDetails);

        return new JwtResponse(token, userDetails);
    }

    @PostMapping("/signup")
    @Operation(summary = "Register new user")
    public UserDto registerUser(@Valid @RequestBody UserDto user, BindingResult result) throws SignUpPasswordComplexityException, UserAlreadyExistsException {
        Person personToSave = modelMapper.map(user, Person.class);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        personToSave.setPassword(passwordEncoder.encode(personToSave.getPassword()));

        Set<String> strRoles = user.getRole();

        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(PRole.ROLE_DISALLOW_EDIT)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    //Example for other roles
                    case "ROLE_ALLOW_EDIT":
                        Role adminRole = roleRepository.findByName(PRole.ROLE_ALLOW_EDIT)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(PRole.ROLE_DISALLOW_EDIT)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        personToSave.setRoles(roles);

        if (result.hasErrors()) {
            List<ObjectError> message = result.getAllErrors();
            throw new SignUpPasswordComplexityException(message.get(0).getDefaultMessage());
        }
        return modelMapper.map(personAuthService.saveUser(personToSave), UserDto.class);
    }

}
