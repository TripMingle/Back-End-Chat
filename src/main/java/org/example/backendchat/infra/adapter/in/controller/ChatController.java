package org.example.backendchat.infra.adapter.in.controller;

import static org.example.backendchat.common.constants.Constants.*;
import static org.example.backendchat.common.result.ResultCode.*;

import org.example.backendchat.application.port.in.ChatInPort;
import org.example.backendchat.common.dto.etc.ChatMessageDTO;
import org.example.backendchat.common.dto.req.GetAllChatMessageReqDTO;
import org.example.backendchat.common.dto.res.GetAllChatMessagesResDTO;
import org.example.backendchat.common.result.ResultResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ChatController {

	private final ChatInPort chatInPort;

	@MessageMapping("/chat/message")
	public void sendMessage(@RequestBody ChatMessageDTO chatMessageDTO) {
		chatInPort.sendMessage(chatMessageDTO);
	}

	@GetMapping("/chat-rooms/{chatRoomId}/messages")
	public ResponseEntity<ResultResponse> getChatMessages(@PathVariable("chatRoomId") Long chatRoomId,
		@RequestParam("page") int page, @RequestParam("type") String type) {
		Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Direction.DESC, SORT_CREATING_CRITERIA));
		GetAllChatMessageReqDTO getAllChatMessageReqDTO = GetAllChatMessageReqDTO.builder()
			.chatRoomId(chatRoomId)
			.chatRoomType(type)
			.build();
		GetAllChatMessagesResDTO chatMessages = chatInPort.getChatMessages(getAllChatMessageReqDTO, pageable);
		return ResponseEntity.ok(ResultResponse.of(GET_ALL_CHAT_MESSAGES, chatMessages));
	}
}
