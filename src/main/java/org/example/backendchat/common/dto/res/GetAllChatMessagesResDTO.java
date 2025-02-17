package org.example.backendchat.common.dto.res;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetAllChatMessagesResDTO {

	private Long chatRoomId;
	private String chatRoomType;
	private List<GetChatMessageResDTO> chatMessages;

}
