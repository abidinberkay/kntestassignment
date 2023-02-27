package com.kntest.knbackend.repository;

import com.kntest.knbackend.model.entity.City;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {

    Page<City> findAll(Pageable pageable);

    Page<City> findAllByNameIsContaining(String name, Pageable pageable);

}
