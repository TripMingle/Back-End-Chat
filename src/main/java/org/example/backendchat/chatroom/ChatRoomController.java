package org.example.backendchat.chatroom;

import org.example.backendchat.chatroom.dto.req.EnterGroupChatRoomReqDTO;
import org.example.backendchat.chatroom.dto.req.EnterOneOnOneChatRoomReqDTO;
import org.example.backendchat.chatroom.dto.req.ExitGroupChatRoomReqDTO;
import org.example.backendchat.chatroom.dto.req.ExitOneOnOneChatRoomReqDTO;
import org.example.backendchat.chatroom.dto.res.EnterGroupChatRoomResDTO;
import org.example.backendchat.chatroom.dto.res.EnterOneOnOneChatRoomResDTO;
import org.example.backendchat.chatroom.dto.res.ExitGroupChatRoomResDTO;
import org.example.backendchat.chatroom.dto.res.ExitOneOnOneChatRoomResDTO;
import org.example.backendchat.common.result.ResultCode;
import org.example.backendchat.common.result.ResultResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat-room")
public class ChatRoomController {

	private final ChatRoomService chatRoomService;

	@PostMapping("/one-on-one/enter")
	public ResponseEntity<ResultResponse> enterOneOnOneChatRoom(
		@RequestBody EnterOneOnOneChatRoomReqDTO enterOneOnOneChatRoomReqDTO) {
		EnterOneOnOneChatRoomResDTO enterOneOnOneChatRoomResDTO = chatRoomService.enterOneOnOneChatRoom(
			enterOneOnOneChatRoomReqDTO);
		return ResponseEntity.ok(
			ResultResponse.of(ResultCode.CREATE_ONE_ON_ONE_CHAT_ROOM_SUCCESS, enterOneOnOneChatRoomResDTO));
	}

	@PostMapping("/group/enter")
	public ResponseEntity<ResultResponse> enterGroupChatRoom(
		@RequestBody EnterGroupChatRoomReqDTO enterGroupChatRoomReqDTO) {
		EnterGroupChatRoomResDTO enterGroupChatRoomResDTO = chatRoomService.enterGroupChatRoom(
			enterGroupChatRoomReqDTO);
		return ResponseEntity.ok(ResultResponse.of(ResultCode.USER_ENTER_CHAT_ROOM_SUCCESS, enterGroupChatRoomResDTO));
	}


	@PatchMapping("/group/exit")
	public ResponseEntity<ResultResponse> exitGroupChatRoom(
		@RequestBody ExitGroupChatRoomReqDTO exitGroupChatRoomReqDTO) {
		ExitGroupChatRoomResDTO exitGroupChatRoomResDTO = chatRoomService.exitGroupChatRoom(exitGroupChatRoomReqDTO);
		return ResponseEntity.ok(ResultResponse.of(ResultCode.EXIT_GROUP_CHAT_ROOM_SUCCESS, exitGroupChatRoomResDTO));
	}

	@PatchMapping("/one-on-one/exit")
	public ResponseEntity<ResultResponse> exitOneOnOneChatRoom(
		@RequestBody ExitOneOnOneChatRoomReqDTO exitOneOnOneChatRoomReqDTO) {
		ExitOneOnOneChatRoomResDTO exitOneOnOneChatRoomResDTO = chatRoomService.exitOneOnOneChatRoom(
			exitOneOnOneChatRoomReqDTO);
		return ResponseEntity.ok(
			ResultResponse.of(ResultCode.EXIT_ONE_ON_ONE_CHAT_ROOM_SUCCESS, exitOneOnOneChatRoomResDTO));
	}
}
