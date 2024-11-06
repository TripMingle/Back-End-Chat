package org.example.backendchat.infra.adapter.out;

import org.example.backendchat.application.port.out.RedisOutPort;
import org.example.backendchat.common.dto.etc.ChatMessageDTO;
import org.example.backendchat.infra.adapter.in.RedisInAdapter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RedisOutAdapter implements RedisOutPort {

	private final RedisTemplate<String, Object> redisTemplate;
	private final RedisInAdapter redisInAdapter;
	private final RedisMessageListenerContainer redisMessageListenerContainer;

	@Override
	public void sendMessage(ChatMessageDTO chatMessageDTO) {
		ChannelTopic channelTopic = new ChannelTopic(
			chatMessageDTO.getChatRoomType().getType() + "/" + chatMessageDTO.getChatRoomId().toString());
		redisMessageListenerContainer.addMessageListener(redisInAdapter, channelTopic);
		redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessageDTO);
	}
}
