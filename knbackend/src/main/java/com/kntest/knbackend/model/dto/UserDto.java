package com.kntest.knbackend.model.dto;

import com.kntest.knbackend.util.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @NotBlank
    private String username;

    @ValidPassword
    private String password;

    private Set<String> role;
}
