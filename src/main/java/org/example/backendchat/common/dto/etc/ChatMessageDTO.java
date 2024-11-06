package org.example.backendchat.common.dto.etc;

import org.example.backendchat.application.entity.ChatRoomType;

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
