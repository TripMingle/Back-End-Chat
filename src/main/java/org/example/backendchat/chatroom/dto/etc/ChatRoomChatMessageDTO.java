package org.example.backendchat.chatroom.dto.etc;

import org.example.backendchat.entity.ChatRoomType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomChatMessageDTO {

	private String message;
	private Long senderId;
	private ChatRoomType chatRoomType;
	private Long chatRoomId;

}
