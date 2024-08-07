package org.example.backendchat.chat;

import org.example.backendchat.chat.dto.ChatMessageDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatController {

	private final ChatService chatService;

	@MessageMapping("/chat/message")
	public void sendMessage(@RequestBody ChatMessageDTO chatMessageDTO) {
		chatService.sendMessage(chatMessageDTO);
	}
}
