package org.example.backendchat.chat.dto;

import org.example.backendchat.entity.ChatRoomType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDTO {

	private String message;
	private Long senderId;
	private ChatRoomType chatRoomType;
	private Long chatRoomId;

}
