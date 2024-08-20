package org.example.backendchat.chatroom.dto.req;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetAllChatMessageReqDTO {

	private String chatRoomType;
	private Long chatRoomId;

}
