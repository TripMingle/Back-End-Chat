package org.example.backendchat.application.service;

import static org.example.backendchat.common.error.ErrorCode.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.example.backendchat.application.entity.ChatMessage;
import org.example.backendchat.application.entity.ChatRoomType;
import org.example.backendchat.application.port.in.ChatInPort;
import org.example.backendchat.application.port.out.ChatRepositoryOutPort;
import org.example.backendchat.application.port.out.RedisOutPort;
import org.example.backendchat.application.port.out.UserRepositoryOutPort;
import org.example.backendchat.common.dto.etc.ChatMessageDTO;
import org.example.backendchat.common.dto.req.GetAllChatMessageReqDTO;
import org.example.backendchat.common.dto.res.GetAllChatMessagesResDTO;
import org.example.backendchat.common.dto.res.GetChatMessageResDTO;
import org.example.backendchat.common.exception.UserNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService implements ChatInPort {

	private final RedisOutPort redisOutPort;
	private final ChatRepositoryOutPort chatRepositoryOutPort;
	private final UserRepositoryOutPort userRepositoryOutPort;

	// 메시지 전송
	public void sendMessage(ChatMessageDTO chatMessageDTO) {
		ChatMessage chatMessage = ChatMessage.builder()
			.message(chatMessageDTO.getMessage())
			.senderId(chatMessageDTO.getSenderId())
			.chatRoomType(chatMessageDTO.getChatRoomType())
			.chatRoomId(chatMessageDTO.getChatRoomId())
			.sendingTime(LocalDateTime.now().toString())
			.build();
		redisOutPort.sendMessage(chatMessageDTO);
		saveChatMessageWithAsync(chatMessage);
	}

	// 메시지 저장 비동기
	@Async
	public void saveChatMessageWithAsync(ChatMessage chatMessage) {
		chatRepositoryOutPort.save(chatMessage);
	}

	// 메시지 조회
	public GetAllChatMessagesResDTO getChatMessages(GetAllChatMessageReqDTO getAllChatMessageReqDTO,
		Pageable pageable) {
		Slice<ChatMessage> chatMessages = chatRepositoryOutPort.findAllByChatRoomIdAndChatRoomType(
			getAllChatMessageReqDTO.getChatRoomId(), ChatRoomType.valueOf(getAllChatMessageReqDTO.getChatRoomType()),
			pageable);
		List<GetChatMessageResDTO> chatMessageResDTOS = chatMessages.stream()
			.map(chat -> GetChatMessageResDTO.builder()
				.userId(chat.getSenderId())
				.userName(userRepositoryOutPort.findById(chat.getSenderId())
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
