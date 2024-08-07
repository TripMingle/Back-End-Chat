package org.example.backendchat.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
	EXAMPLE(200, "EX001", "예시 결과코드입니다."),

	// chatroom
	ENTER_CHAT_ROOM_SUCCESS(200, "GC001", "채팅방 입장 성공"),
	;
	private final int status;
	private final String code;
	private final String message;
}
