package org.example.backendchat.chatroom;

import org.example.backendchat.common.config.websocket.ChatRoomUserForRedisHash;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatRoomRedisRepository {

	private static final String ENTER_INFO = "ENTER_INFO";
	private static final String ENTER_USER_INFO = "ENTER_USER_INFO";

	@Resource(name = "redisTemplate")
	private HashOperations<String, String, ChatRoomUserForRedisHash> hashOpsEnterInfo;
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, String> hashOpsEnterUserInfo;
	private final ObjectMapper objectMapper;

	public void saveUserChatRoomEnterInfo(String sessionId, ChatRoomUserForRedisHash chatRoomUserForRedisHash) {
		hashOpsEnterInfo.put(ENTER_INFO, sessionId, chatRoomUserForRedisHash);
	}

	public void saveUserToken(String sessionId, String token) {
		hashOpsEnterUserInfo.put(ENTER_USER_INFO, sessionId, token);
	}

	@Async
	public void deleteUserChatRoomEnterInfo(String sessionId) {
		if (hashOpsEnterInfo.hasKey(ENTER_INFO, sessionId)) {
			hashOpsEnterInfo.delete(ENTER_INFO, sessionId);
		}
	}

	@Async
	public void deleteUserToken(String sessionId) {
		if (hashOpsEnterUserInfo.hasKey(ENTER_USER_INFO, sessionId)) {
			hashOpsEnterUserInfo.delete(ENTER_USER_INFO, sessionId);
		}
	}

	public ChatRoomUserForRedisHash getUserChatRoomEnterInfo(String sessionId) {
		Object rawChatRoomUserForRedisHash = hashOpsEnterInfo.get(ENTER_INFO, sessionId);
		return objectMapper.convertValue(rawChatRoomUserForRedisHash,
			ChatRoomUserForRedisHash.class);
	}

	public String getTokenByRedisHash(String sessionId) {
		return hashOpsEnterUserInfo.get(ENTER_USER_INFO, sessionId);
	}

}
