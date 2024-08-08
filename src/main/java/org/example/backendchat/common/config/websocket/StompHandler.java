package org.example.backendchat.common.config.websocket;

import static org.example.backendchat.common.error.ErrorCode.*;

import org.example.backendchat.chatroom.ChatRoomRedisRepository;
import org.example.backendchat.chatroom.GroupChatRoomRepository;
import org.example.backendchat.chatroom.OneOnOneChatRoomRepository;
import org.example.backendchat.chatroomuser.ChatRoomUserRepository;
import org.example.backendchat.common.exception.ChatRoomNotFoundException;
import org.example.backendchat.common.exception.InvalidChatRoomUserException;
import org.example.backendchat.common.utils.JwtUtils;
import org.example.backendchat.entity.ChatRoomType;
import org.example.backendchat.entity.ChatRoomUser;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

	private final JwtUtils jwtUtils;
	private final GroupChatRoomRepository groupChatRoomRepository;
	private final OneOnOneChatRoomRepository oneOnOneChatRoomRepository;
	private final ChatRoomUserRepository chatRoomUserRepository;
	private final ChatRoomRedisRepository chatRoomRedisRepository;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
		if (StompCommand.CONNECT.equals(accessor.getCommand())) {
			String accessToken = jwtUtils.parsingAccessToken(accessor.getFirstNativeHeader("Authorization"));
			jwtUtils.validateToken(accessToken);
		} else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
			ChatRoomType chatRoomType = ChatRoomType.valueOf(accessor.getDestination().split("/")[4]);
			Long chatRoomId = Long.parseLong(accessor.getDestination().split("/")[5]);
			String userSessionId = accessor.getSessionId();

			validateExistedChatRoom(chatRoomType, chatRoomId);

			ChatRoomUser chatRoomUser = chatRoomUserRepository.findByChatRoomIdAndChatRoomType(chatRoomId, chatRoomType)
				.orElseThrow(() -> new InvalidChatRoomUserException("Invalid ChatRoom User", INVALID_CHAT_ROOM_USER));
			chatRoomUser.changeConnectionState(true);
			chatRoomUserRepository.save(chatRoomUser);

			chatRoomRedisRepository.saveUserChatRoomEnterInfo(userSessionId, changedChatRoomUser);
		} else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
			String userSessionId = accessor.getSessionId();
			ChatRoomUser chatRoomUser = chatRoomRedisRepository.getUserChatRoomEnterInfo(userSessionId);
			chatRoomUser.changeConnectionState(false);
			chatRoomUserRepository.save(chatRoomUser);

			chatRoomRedisRepository.deleteUserAllEnterInfo(userSessionId);
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

}
