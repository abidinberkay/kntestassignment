package com.kntest.knbackend.UnitTests.CityTest;

import com.kntest.knbackend.controller.CityController;
import com.kntest.knbackend.exception.CityNotFoundException;
import com.kntest.knbackend.model.dto.CityDto;
import com.kntest.knbackend.model.entity.City;
import com.kntest.knbackend.service.CityService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CityControllerTest {

    private static final String CITY_NAME = "Test City";
    private static final String PHOTO = "Photo";

    @Mock
    private CityService cityService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CityController cityController;

    @Test
    public void cityControllerTest_testGetCityPage_returnCityPage() {

        //Given
        List<City> cities = generateCityList();
        Pageable pageable = Pageable.ofSize(10).withPage(0);
        Page<City> page = new PageImpl<>(cities, pageable, cities.size());
        City testCity = new City(1, CITY_NAME, PHOTO);
        when(cityService.findAllAsPage(anyString(), eq(pageable))).thenReturn(page);

        //When
        ResponseEntity<Page<City>> responseEntity = cityController.getCityPage(testCity.getName(), pageable);

        //Then
        verify(cityService, times(1)).findAllAsPage(eq(testCity.getName()), eq(pageable));
        Assertions.assertThat(HttpStatus.OK).isEqualTo(responseEntity.getStatusCode());
        Assertions.assertThat(page).isEqualTo(responseEntity.getBody());
    }

    @Test
    public void cityControllerTest_testUpdateCity() throws CityNotFoundException {

        //Given
        CityDto cityDto = CityDto.builder()
                .id(1)
                .name(CITY_NAME)
                .photo(PHOTO)
                .build();

        City city = City.builder()
                .id(1)
                .name(CITY_NAME)
                .photo(PHOTO)
                .build();

        when(modelMapper.map(cityDto, City.class)).thenReturn(new City(1, CITY_NAME, PHOTO));
        when(modelMapper.map(city, CityDto.class)).thenReturn(new CityDto(1, CITY_NAME, PHOTO));
        when(cityService.updateCity(any(City.class))).thenReturn(new City(1, CITY_NAME, PHOTO));

        //When
        ResponseEntity<CityDto> responseEntity = cityController.updateCity(cityDto);

        //Then
        verify(modelMapper, times(1)).map(cityDto, City.class);
        verify(cityService, times(1)).updateCity(any(City.class));
        Assertions.assertThat(HttpStatus.OK).isEqualTo(responseEntity.getStatusCode());
        Assertions.assertThat(cityDto).isEqualTo(responseEntity.getBody());
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
}
