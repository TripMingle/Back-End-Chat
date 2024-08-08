package org.example.backendchat.chat.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EncryptedChatMessageDTO {

	private String message;
	private String senderId;
	private String chatRoomType;
	private String chatRoomId;

}
