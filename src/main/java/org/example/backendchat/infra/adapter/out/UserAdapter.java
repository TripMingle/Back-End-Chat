package org.example.backendchat.infra.adapter.out;

import java.util.Optional;

import org.example.backendchat.application.entity.User;
import org.example.backendchat.application.port.out.UserRepositoryOutPort;
import org.example.backendchat.infra.adapter.out.repository.UserRepository;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserAdapter implements UserRepositoryOutPort {
	private final UserRepository userRepository;

	@Override
	public Optional<User> findById(Long senderId) {
		return userRepository.findById(senderId);
	}
}
