package com.kntest.knbackend.controller;

import com.kntest.knbackend.exception.CityNotFoundException;
import com.kntest.knbackend.model.dto.CityDto;
import com.kntest.knbackend.model.entity.City;
import com.kntest.knbackend.service.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/city")
@CrossOrigin
public class CityController {

    private final CityService cityService;
    private final ModelMapper modelMapper;

    public CityController(CityService cityService,
                          ModelMapper modelMapper) {
        this.cityService = cityService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/page")
    @Operation(summary = "Get city page with optional name parameter to search by name", security = @SecurityRequirement(name = "BearerJWT"))
    public ResponseEntity<Page<City>> getCityPage(@RequestParam(required = false) String name, Pageable pageable) {
        return ResponseEntity.ok(cityService.findAllAsPage(name, pageable));
    }

    @PreAuthorize("hasRole('ROLE_ALLOW_EDIT')")
    @PutMapping
    @Operation(summary = "Update city information", security = @SecurityRequirement(name = "BearerJWT"))
    public ResponseEntity<CityDto> updateCity(@Valid @RequestBody CityDto cityDto) throws CityNotFoundException {
        City city = cityService.updateCity(modelMapper.map(cityDto, City.class));
        return ResponseEntity.ok(modelMapper.map(city, CityDto.class));
    }
}
