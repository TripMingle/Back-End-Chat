package org.example.backendchat.application.port.out;

import org.example.backendchat.application.entity.ChatMessage;
import org.example.backendchat.application.entity.ChatRoomType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ChatRepositoryOutPort {
	void save(ChatMessage chatMessage);

	Slice<ChatMessage> findAllByChatRoomIdAndChatRoomType(Long chatRoomId, ChatRoomType chatRoomType,
		Pageable pageable);
}
