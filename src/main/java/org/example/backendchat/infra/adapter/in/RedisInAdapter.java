package org.example.backendchat.infra.adapter.in;

import static org.example.backendchat.common.error.ErrorCode.*;

import org.example.backendchat.common.dto.etc.ChatMessageDTO;
import org.example.backendchat.common.exception.ChatMessageSendingException;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisInAdapter implements MessageListener {

	private final SimpMessageSendingOperations messageTemplate;
	private final ObjectMapper objectMapper;
	private final RedisTemplate<String, Object> redisTemplate;

	@Override
	public void onMessage(Message message, byte[] pattern) {
		try {
			String publishedMessage = redisTemplate.getStringSerializer().deserialize(message.getBody());
			ChatMessageDTO chatMessageDTO = objectMapper.readValue(publishedMessage, ChatMessageDTO.class);
			messageTemplate.convertAndSend(
				"/sub/chat/room/" + chatMessageDTO.getChatRoomType().getType() + "/" + chatMessageDTO.getChatRoomId(),
				chatMessageDTO);
		} catch (Exception e) {
			throw new ChatMessageSendingException("Failed to send chat message", CHAT_MESSAGE_SENDING_FAILED);
		}
	}
}
