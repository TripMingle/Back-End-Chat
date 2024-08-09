package org.example.backendchat.common.config.websocket;

import org.example.backendchat.entity.ChatRoomType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomUserForRedisHash {

	private Long userId;
	private Long chatRoomId;
	private ChatRoomType chatRoomType;

}
