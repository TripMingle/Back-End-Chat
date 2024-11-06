package org.example.backendchat.chat;

import static org.example.backendchat.common.error.ErrorCode.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.example.backendchat.chat.dto.etc.ChatMessageDTO;
import org.example.backendchat.chat.dto.req.GetAllChatMessageReqDTO;
import org.example.backendchat.chat.dto.res.GetAllChatMessagesResDTO;
import org.example.backendchat.chat.dto.res.GetChatMessageResDTO;
import org.example.backendchat.common.exception.UserNotFoundException;
import org.example.backendchat.entity.ChatMessage;
import org.example.backendchat.entity.ChatRoomType;
import org.example.backendchat.redis.RedisSubscriber;
import org.example.backendchat.user.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
	private final UserRepository userRepository;
	private final RedisTemplate<String, Object> redisTemplate;
	private final RedisSubscriber redisSubscriber;
	private final RedisMessageListenerContainer redisMessageListenerContainer;

	// 메시지 전송
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
		redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessageDTO);
	}

	// 메시지 저장 비동기
	@Async
	public void saveChatMessageWithAsync(ChatMessage chatMessage) {
		chatMongoRepository.save(chatMessage);
	}

	// 메시지 조회
	public GetAllChatMessagesResDTO getChatMessages(GetAllChatMessageReqDTO getAllChatMessageReqDTO,
		Pageable pageable) {
		Slice<ChatMessage> chatMessages = chatMongoRepository.findAllByChatRoomIdAndChatRoomType(
			getAllChatMessageReqDTO.getChatRoomId(), ChatRoomType.valueOf(getAllChatMessageReqDTO.getChatRoomType()),
			pageable);
		List<GetChatMessageResDTO> chatMessageResDTOS = chatMessages.stream()
			.map(chat -> GetChatMessageResDTO.builder()
				.userId(chat.getSenderId())
				.userName(userRepository.findById(chat.getSenderId())
					.orElseThrow(() -> new UserNotFoundException("User not found", USER_NOT_FOUND))
					.getNickName())
				.message(chat.getMessage())
				.sendingTime(chat.getSendingTime())
				.build())
			.collect(Collectors.toList());
		return GetAllChatMessagesResDTO.builder()
			.chatMessages(chatMessageResDTOS)
			.chatRoomId(getAllChatMessageReqDTO.getChatRoomId())
			.chatRoomType(getAllChatMessageReqDTO.getChatRoomType())
			.build();
	}
}
