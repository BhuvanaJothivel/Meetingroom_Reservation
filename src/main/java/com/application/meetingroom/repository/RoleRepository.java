package com.application.meetingroom.repository;

import com.application.meetingroom.model.ERole;
import com.application.meetingroom.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
