package org.example.backendchat.common.config.websocket;

import static org.example.backendchat.common.error.ErrorCode.*;

import org.example.backendchat.chatroom.ChatRoomRedisRepository;
import org.example.backendchat.chatroom.GroupChatRoomRepository;
import org.example.backendchat.chatroom.OneOnOneChatRoomRepository;
import org.example.backendchat.chatroomuser.ChatRoomUserRepository;
import org.example.backendchat.common.exception.ChatRoomNotFoundException;
import org.example.backendchat.common.exception.InvalidAccessTokenException;
import org.example.backendchat.common.exception.InvalidChatRoomUserException;
import org.example.backendchat.common.exception.UserNotFoundException;
import org.example.backendchat.common.utils.JwtUtils;
import org.example.backendchat.entity.ChatRoomType;
import org.example.backendchat.entity.ChatRoomUser;
import org.example.backendchat.entity.User;
import org.example.backendchat.user.UserRepository;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

	private final JwtUtils jwtUtils;
	private final GroupChatRoomRepository groupChatRoomRepository;
	private final OneOnOneChatRoomRepository oneOnOneChatRoomRepository;
	private final ChatRoomUserRepository chatRoomUserRepository;
	private final ChatRoomRedisRepository chatRoomRedisRepository;
	private final UserRepository userRepository;
	private final ObjectMapper objectMapper;
	private final PropertyPlaceholderAutoConfiguration propertyPlaceholderAutoConfiguration;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		if (StompCommand.CONNECT.equals(accessor.getCommand())) {
			String rawAccessToken = accessor.getFirstNativeHeader("Authorization");
			validateNullAccessToken(rawAccessToken);
			String accessToken = jwtUtils.parsingAccessToken(rawAccessToken);
			String sessionId = (String) accessor.getHeader("simpSessionId");
			chatRoomRedisRepository.saveUserToken(sessionId, accessToken);
			jwtUtils.validateToken(accessToken);
			log.info("CONNECT");
		} else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
			ChatRoomType chatRoomType = ChatRoomType.valueOf(accessor.getDestination().split("/")[4]);
			Long chatRoomId = Long.parseLong(accessor.getDestination().split("/")[5]);
			String userSessionId = accessor.getSessionId();
			String accessToken = chatRoomRedisRepository.getTokenByRedisHash(userSessionId);
			chatRoomRedisRepository.deleteUserToken(userSessionId);
			User user = userRepository.findByEmail(jwtUtils.getEmail(accessToken))
				.orElseThrow(() -> new UserNotFoundException("User Not Found", USER_NOT_FOUND));
			validateExistedChatRoom(chatRoomType, chatRoomId);
			ChatRoomUser chatRoomUser = chatRoomUserRepository.findByChatRoomIdAndChatRoomTypeAndUserId(chatRoomId, chatRoomType, user.getId())
				.orElseThrow(() -> new InvalidChatRoomUserException("Invalid ChatRoom User", INVALID_CHAT_ROOM_USER));
			chatRoomUser.changeConnectionState(true);
			chatRoomUserRepository.save(chatRoomUser);
			ChatRoomUserForRedisHash chatRoomUserForRedisHash = convertChatRoomUserToChatRoomUserForRedisHash(chatRoomUser);
			chatRoomRedisRepository.saveUserChatRoomEnterInfo(userSessionId, chatRoomUserForRedisHash);
			log.info("SUBSCRIBE");
		} else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
			String sessionId = accessor.getSessionId();
			ChatRoomUserForRedisHash chatRoomUserForRedisHash = chatRoomRedisRepository.getUserChatRoomEnterInfo(sessionId);
			ChatRoomUser chatRoomUser = chatRoomUserRepository.findByChatRoomIdAndChatRoomTypeAndUserId(chatRoomUserForRedisHash.getChatRoomId(), chatRoomUserForRedisHash.getChatRoomType(), chatRoomUserForRedisHash.getUserId())
				.orElseThrow(() -> new InvalidChatRoomUserException("Invalid ChatRoom User", INVALID_CHAT_ROOM_USER));
			chatRoomUser.changeConnectionState(false);
			chatRoomUserRepository.save(chatRoomUser);
			chatRoomRedisRepository.deleteUserChatRoomEnterInfo(sessionId);
			log.info("DISCONNECT");
		}
		return message;
	}

	private void validateExistedChatRoom(ChatRoomType chatRoomType, Long chatRoomId) {
		if (chatRoomType == ChatRoomType.GROUP) {
			groupChatRoomRepository.findById(chatRoomId).orElseThrow(() -> new ChatRoomNotFoundException("ChatRoom not found", CHAT_ROOM_NOT_FOUND));
		} else if (chatRoomType == ChatRoomType.ONE_ON_ONE) {
			oneOnOneChatRoomRepository.findById(chatRoomId).orElseThrow(() ->  new ChatRoomNotFoundException("ChatRoom not found", CHAT_ROOM_NOT_FOUND));
		} else {
			throw new IllegalArgumentException("ChatRoomType not found");
		}
	}

	private ChatRoomUserForRedisHash convertChatRoomUserToChatRoomUserForRedisHash(ChatRoomUser chatRoomUser) {
		return ChatRoomUserForRedisHash.builder()
			.userId(chatRoomUser.getUser().getId())
			.chatRoomId(chatRoomUser.getChatRoomId())
			.chatRoomType(chatRoomUser.getChatRoomType())
			.build();
	}

	private void validateNullAccessToken(String accessToken) {
		if (accessToken == null) {
			throw new InvalidAccessTokenException("Token not found", ACCESS_TOKEN_NOT_FOUND);
		}
	}

}
