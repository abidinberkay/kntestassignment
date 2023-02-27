package com.kntest.knbackend.repository;

import com.kntest.knbackend.enums.PRole;
import com.kntest.knbackend.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    Optional<Role> findByName(PRole name);
}
