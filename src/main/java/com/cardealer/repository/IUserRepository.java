package com.cardealer.repository;

import com.cardealer.model.entity.Role;
import com.cardealer.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface IUserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsernameAndPassword(String username, String password);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    @Query(
            value = "select u from User u where u.username = ?1 or u.email = ?1 or u.phone = ?1"
    )
    Optional<User> loadByUsername(String username);


    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
    Page<User> findAll(Pageable pageable);
    Page<User> findByFullNameContaining(String name, Pageable pageable);



}
