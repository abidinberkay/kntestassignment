package com.kntest.knbackend.UnitTests.CityTest;

import com.kntest.knbackend.exception.CityNotFoundException;
import com.kntest.knbackend.model.entity.City;
import com.kntest.knbackend.repository.CityRepository;
import com.kntest.knbackend.service.CityService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityService cityService;

    @Test
    void cityServiceTest_testFindAllAsPageWithName_shouldReturnPagedCities() {

        //Given
        List<City> cities = generateCityList();
        int pageNumber = 0;
        int pageSize = 5;
        PageRequest pageable = setUpPageRequestObject(pageNumber, pageSize);
        when(cityRepository.findAllByNameIsContaining(anyString(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(cities, pageable, cities.size()));

        // When
        Page<City> pageResult = cityService.findAllAsPage("City", pageable);

        // Then
        Assertions.assertThat(pageResult).isNotNull();
        Assertions.assertThat(pageResult.getNumber()).isEqualTo(pageNumber);
        Assertions.assertThat(pageResult.getSize()).isEqualTo(pageSize);
        Assertions.assertThat(pageResult.getTotalElements()).isEqualTo(cities.size());
        Assertions.assertThat(pageResult.getTotalPages()).isEqualTo(cities.size() / pageSize);
    }

    @Test
    public void cityServiceTest_testUpdateCity_shouldUpdateCity() throws CityNotFoundException {

        //Given
        City testCity = new City(1, "CITY_NAME", "PHOTO");
        when(cityRepository.findById(1)).thenReturn(Optional.of(testCity));
        when(cityRepository.save(any())).thenReturn(testCity);

        //When
        City updatedCity = cityService.updateCity(testCity);

        //Then
        verify(cityRepository, times(1)).findById(1);
        verify(cityRepository, times(1)).save(testCity);
        Assertions.assertThat(testCity).isEqualTo(updatedCity);
    }

    private List<City> generateCityList() {
        List<City> cityList = new ArrayList<>();
        for (int i = 1; i <= 40; i++) {
            City city = new City();
            city.setName("City " + i);
            city.setPhoto("Link " + i);
            cityList.add(city);
        }
        return cityList;
    }

    private PageRequest setUpPageRequestObject(int pageNumber, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNumber, pageSize);
        return pageable;
    }


}
