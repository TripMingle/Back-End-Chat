package org.example.backendchat.common.dto.req;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetAllChatMessageReqDTO {

	private String chatRoomType;
	private Long chatRoomId;

}
