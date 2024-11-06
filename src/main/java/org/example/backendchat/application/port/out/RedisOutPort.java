package org.example.backendchat.application.port.out;

import org.example.backendchat.common.dto.etc.ChatMessageDTO;

public interface RedisOutPort {
	void sendMessage(ChatMessageDTO chatMessageDTO);
}
