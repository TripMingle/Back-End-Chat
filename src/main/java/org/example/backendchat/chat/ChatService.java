package org.example.backendchat.chat;

import java.time.LocalDateTime;

import org.example.backendchat.chat.dto.ChatMessageDTO;
import org.example.backendchat.entity.ChatMessage;
import org.example.backendchat.redis.RedisPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

	private final ChatMongoRepository chatMongoRepository;
	private final RedisTemplate<String, Object> redisTemplate;
	private final RedisPublisher redisPublisher;

	public void sendMessage(ChatMessageDTO chatMessageDTO) {
		ChatMessage chatMessage = ChatMessage.builder()
			.message(chatMessageDTO.getMessage())
			.senderId(chatMessageDTO.getSenderId())
			.chatRoomType(chatMessageDTO.getChatRoomType())
			.chatRoomId(chatMessageDTO.getChatRoomId())
			.sendingTime(LocalDateTime.now().toString())
			.build();
		saveChatMessageWithAsync(chatMessage);
		redisPublisher.publish(new ChannelTopic((chatMessageDTO.getChatRoomId().toString())), chatMessageDTO);
		log.info("############ publish #############");
		// redisTemplate.convertAndSend(ChannelTopic.of(chatMessageDTO.getChatRoomId().toString()).getTopic(), chatMessageDTO);
	}

	@Async
	public void saveChatMessageWithAsync(ChatMessage chatMessage) {
		chatMongoRepository.save(chatMessage);
	}
}
