package com.project.movies.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<UserModel, String> {
    Optional<UserModel> findByEmail(String email);

    Optional<UserModel> findByUsername(String username);

    void deleteByUsername(String username);
}
