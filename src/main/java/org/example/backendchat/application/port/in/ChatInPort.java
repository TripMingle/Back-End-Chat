package org.example.backendchat.application.port.in;

import org.example.backendchat.common.dto.etc.ChatMessageDTO;
import org.example.backendchat.common.dto.req.GetAllChatMessageReqDTO;
import org.example.backendchat.common.dto.res.GetAllChatMessagesResDTO;
import org.springframework.data.domain.Pageable;

public interface ChatInPort {
	void sendMessage(ChatMessageDTO chatMessageDTO);

	GetAllChatMessagesResDTO getChatMessages(GetAllChatMessageReqDTO getAllChatMessageReqDTO, Pageable pageable);
}
