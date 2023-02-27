package com.kntest.knbackend.service;

import com.kntest.knbackend.exception.CityNotFoundException;
import org.apache.commons.lang3.StringUtils;
import com.kntest.knbackend.model.entity.City;
import com.kntest.knbackend.repository.CityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CityService {

    private final String CITY_NOT_FOUND_MSG = "City not found with id: ";
    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public Page<City> findAllAsPage(String name, Pageable pageable) {
        if (StringUtils.isNotBlank(name))
            return cityRepository.findAllByNameIsContaining(name, pageable);
        else
            return cityRepository.findAll(pageable);
    }

    public City updateCity(City city) throws CityNotFoundException {
        cityRepository.findById(city.getId())
                .orElseThrow(() -> new CityNotFoundException(CITY_NOT_FOUND_MSG + city.getId()));
        return cityRepository.save(city);
    }

}
