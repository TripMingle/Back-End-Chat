package org.example.backendchat.infra.adapter.out;

import org.example.backendchat.application.entity.ChatMessage;
import org.example.backendchat.application.entity.ChatRoomType;
import org.example.backendchat.application.port.out.ChatRepositoryOutPort;
import org.example.backendchat.infra.adapter.out.repository.ChatMongoRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatAdapter implements ChatRepositoryOutPort {
	private final ChatMongoRepository chatMongoRepository;

	@Override
	public void save(ChatMessage chatMessage) {
		chatMongoRepository.save(chatMessage);
	}

	@Override
	public Slice<ChatMessage> findAllByChatRoomIdAndChatRoomType(Long chatRoomId, ChatRoomType chatRoomType,
		Pageable pageable) {
		return chatMongoRepository.findAllByChatRoomIdAndChatRoomType(chatRoomId, chatRoomType, pageable);
	}
}
