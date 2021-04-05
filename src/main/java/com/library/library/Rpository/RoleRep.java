package com.library.library.Rpository;

import com.library.library.Model.ERole;
import com.library.library.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRep extends JpaRepository<Role, Long> {

    Optional<Role> findByName(ERole name);

}
