package org.example.backendchat.chatroom.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetChatMessageResDTO {

	private Long userId;
	private String userName;
	private String message;
	private String sendingTime;

}
