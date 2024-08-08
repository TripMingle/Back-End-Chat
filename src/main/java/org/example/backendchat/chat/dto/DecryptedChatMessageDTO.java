package org.example.backendchat.chat.dto;

import org.example.backendchat.entity.ChatRoomType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DecryptedChatMessageDTO {

	private String message;
	private Long senderId;
	private ChatRoomType chatRoomType;
	private Long chatRoomId;

}
