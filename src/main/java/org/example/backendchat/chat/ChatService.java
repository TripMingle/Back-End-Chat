package org.example.backendchat.chat;

import java.time.LocalDateTime;

import org.example.backendchat.chat.dto.ChatMessageDTO;
import org.example.backendchat.chat.dto.EncryptedChatMessageDTO;
import org.example.backendchat.common.utils.MessageCryptUtils;
import org.example.backendchat.entity.ChatMessage;
import org.example.backendchat.redis.RedisSubscriber;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
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
	private final RedisSubscriber redisSubscriber;
	private final RedisMessageListenerContainer redisMessageListenerContainer;
	private final MessageCryptUtils messageCryptUtils;

	public void sendMessage(ChatMessageDTO chatMessageDTO) {
		ChatMessage chatMessage = ChatMessage.builder()
			.message(chatMessageDTO.getMessage())
			.senderId(chatMessageDTO.getSenderId())
			.chatRoomType(chatMessageDTO.getChatRoomType())
			.chatRoomId(chatMessageDTO.getChatRoomId())
			.sendingTime(LocalDateTime.now().toString())
			.build();
		ChannelTopic channelTopic = new ChannelTopic(
			chatMessageDTO.getChatRoomType().getType() + "/" + chatMessageDTO.getChatRoomId().toString());
		redisMessageListenerContainer.addMessageListener(redisSubscriber, channelTopic);
		saveChatMessageWithAsync(chatMessage);
		EncryptedChatMessageDTO encryptedChatMessageDTO = messageCryptUtils.encryptChatMessage(chatMessageDTO);
		redisTemplate.convertAndSend(channelTopic.getTopic(), encryptedChatMessageDTO);
	}

	@Async
	public void saveChatMessageWithAsync(ChatMessage chatMessage) {
		chatMongoRepository.save(chatMessage);
	}
}
