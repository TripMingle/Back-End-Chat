package org.example.backendchat.infra.adapter.out.repository;

import java.util.Optional;

import org.example.backendchat.application.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
}
