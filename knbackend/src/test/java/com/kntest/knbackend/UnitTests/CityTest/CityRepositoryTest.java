package com.kntest.knbackend.UnitTests.CityTest;

import com.kntest.knbackend.model.entity.City;
import com.kntest.knbackend.repository.CityRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.TestPropertySource;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CityRepositoryTest {

    @Autowired
    private CityRepository cityRepository;

    @BeforeEach
    private void populateCities() {
        for (int i = 1; i <= 40; i++) {
            City city = new City();
            city.setName("City " + i);
            city.setPhoto("Link " + i);
            cityRepository.save(city);
        }
    }

    @Test
    public void CityRepositoryTest_findAll_ReturnPageOfCities() {

        //Given
        PageRequest pageRequest = setUpPageRequestObject(0, 5);

        //When
        Page<City> cityPage = cityRepository.findAll(pageRequest);

        //Then
        Assertions.assertThat(cityPage.getTotalPages()).isEqualTo(8);
        Assertions.assertThat(cityPage.getTotalElements()).isEqualTo(40);
        Assertions.assertThat(cityPage.getContent().get(0).getName()).isEqualTo("City 1");

        List<String> cityNames = cityPage.getContent().stream()
                .map(City::getName)
                .collect(Collectors.toList());
        Assertions.assertThat(cityNames).containsExactly("City 1", "City 10", "City 11", "City 12", "City 13");
    }

    @Test
    public void CityRepositoryTest_findAllByNameIsContaining_ReturnPageOfCitiesContainingName() {

        //Given
        PageRequest pageRequest = setUpPageRequestObject(0, 15);
        String nameToTest = "ity 1";

        //When
        Page<City> cityPage = cityRepository.findAllByNameIsContaining(nameToTest, pageRequest);

        //Then
        Assertions.assertThat(cityPage.getTotalPages()).isEqualTo(1);
        Assertions.assertThat(cityPage.getTotalElements()).isEqualTo(11);
        Assertions.assertThat(cityPage.getContent().get(0).getName()).isEqualTo("City 1");

        List<String> cityNames = cityPage.getContent().stream()
                .map(City::getName)
                .collect(Collectors.toList());
        Assertions.assertThat(cityNames).containsExactly("City 1",
                "City 10",
                "City 11",
                "City 12",
                "City 13",
                "City 14",
                "City 15",
                "City 16",
                "City 17",
                "City 18",
                "City 19");
    }

    private PageRequest setUpPageRequestObject(int pageNumber, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");
        PageRequest pageable = PageRequest.of(pageNumber, pageSize, sort);
        return pageable;
    }
}
