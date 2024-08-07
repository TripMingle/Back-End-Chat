package org.example.backendchat.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Getter
@Document(collection = "chat")
public class ChatMessage {

	@Id
	private String id;

	private String message;

	private Long senderId;

	private ChatRoomType chatRoomType;

	private Long chatRoomId;

	private String sendingTime;

	@Builder
	public ChatMessage(String message, Long senderId, ChatRoomType chatRoomType, Long chatRoomId,
		String sendingTime) {
		this.message = message;
		this.senderId = senderId;
		this.chatRoomType = chatRoomType;
		this.chatRoomId = chatRoomId;
		this.sendingTime = sendingTime;
	}
}
