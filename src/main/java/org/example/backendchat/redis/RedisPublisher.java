package org.example.backendchat.redis;

import org.example.backendchat.chat.dto.ChatMessageDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisPublisher {

	private final RedisTemplate<String, Object> redisTemplate;
	private final RedisSubscriber redisSubscriber;
	private final RedisMessageListenerContainer redisMessageListenerContainer;

	public void publish(ChannelTopic topic, ChatMessageDTO chatMessageDTO) {
		redisMessageListenerContainer.addMessageListener(redisSubscriber, new ChannelTopic(chatMessageDTO.getChatRoomId().toString()));
		redisTemplate.convertAndSend(topic.getTopic(), chatMessageDTO);
	}

}
