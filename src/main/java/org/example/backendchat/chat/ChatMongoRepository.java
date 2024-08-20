package org.example.backendchat.chat;

import org.example.backendchat.entity.ChatMessage;
import org.example.backendchat.entity.ChatRoomType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMongoRepository extends MongoRepository<ChatMessage, String> {
	Long countByChatRoomIdAndChatRoomType(Long id, ChatRoomType chatRoomType);

	Slice<ChatMessage> findAllByChatRoomIdAndChatRoomType(Long chatRoomId, ChatRoomType chatRoomType,
		Pageable pageable);
}
