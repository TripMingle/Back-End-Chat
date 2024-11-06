package org.example.backendchat.application.port.out;

import java.util.Optional;

import org.example.backendchat.application.entity.User;

public interface UserRepositoryOutPort {
	Optional<User> findById(Long senderId);
}
