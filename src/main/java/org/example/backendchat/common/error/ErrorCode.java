package org.example.backendchat.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	//Example
	EXAMPLE(400, "EX001", "예시 에러코드입니다."),

	// user
	USER_NOT_FOUND(404, "U001", "해당 유저가 존재하지 않습니다."),

	//Board
	BOARD_NOT_FOUND(400, "B001", "게시물을 찾을 수 없습니다."),

	// chatroom
	INVALID_GROUP_CHAT_ROOM(400, "GC001", "그룹 채팅방이 유효하지 않습니다."),
	CHAT_ROOM_NOT_FOUND(400, "GC002", "채팅방을 찾을 수 없습니다."),

	// chat
	CHAT_MESSAGE_SENDING_FAILED(400, "CH001", "채팅 메시지 전송에 실패하였습니다."),

	// chat room user
	INVALID_CHAT_ROOM_USER(400, "CRU001", "채팅방 유저가 유효하지 않습니다."),

	// auth
	ACCESS_TOKEN_NOT_FOUND(404, "AT001", "액세스토큰을 입력받지 못했습니다.")
	;

	final private int status;
	final private String ErrorCode;
	final private String message;
}
