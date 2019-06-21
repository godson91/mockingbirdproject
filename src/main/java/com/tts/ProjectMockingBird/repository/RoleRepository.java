package com.tts.ProjectMockingBird.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tts.ProjectMockingBird.model.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long>{

    Role findByRole(String role);

}
