package org.example.backendchat.chatroom;

import org.example.backendchat.entity.ChatRoomUser;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import jakarta.annotation.Resource;

@Repository
public class ChatRoomRedisRepository {

	private static final String ENTER_INFO = "ENTER_INFO";

	@Resource(name = "redisTemplate")
	private HashOperations<String, String, ChatRoomUser> hashOpsEnterInfo;

	@Async
	public void saveUserChatRoomEnterInfo(String sessionId, ChatRoomUser chatRoomUser) {
		hashOpsEnterInfo.put(ENTER_INFO, sessionId, chatRoomUser);
	}

	@Async
	public void deleteUserAllEnterInfo(String sessionId) {
		if (hashOpsEnterInfo.hasKey(ENTER_INFO, sessionId)) {
			hashOpsEnterInfo.delete(ENTER_INFO, sessionId);
		}
	}

	public ChatRoomUser getUserChatRoomEnterInfo(String sessionId) {
		return hashOpsEnterInfo.get(ENTER_INFO, sessionId);
	}

}
