package com.project.InsureAI.repository;

import com.project.InsureAI.entity.MyUser;
import com.project.InsureAI.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MyUserRepository extends JpaRepository<MyUser, Long> {

    Optional<MyUser> findByEmail(String email);

    List<MyUser> findByRoleAndIsActiveTrue(Role role);
}
