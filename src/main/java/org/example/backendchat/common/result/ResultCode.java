package org.example.backendchat.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {
	EXAMPLE(200, "EX001", "예시 결과코드입니다."),

	// chat
	GET_ALL_CHAT_MESSAGES(200, "CM001", "전체 채팅 조회 성공"),

	// chat room
	CREATE_ONE_ON_ONE_CHAT_ROOM_SUCCESS(200, "O3CR001", "일대일 채팅방 생성 성공"),
	CREATE_GROUP_CHAT_ROOM_SUCCESS(200, "GCR001", "그룹 채팅방 생성 성공"),
	USER_ENTER_CHAT_ROOM_SUCCESS(200, "GCR002", "그룹 채팅방 참여 성공"),
	EXIT_GROUP_CHAT_ROOM_SUCCESS(200, "GCR003", "그룹 채팅방 퇴장 성공"),
	EXIT_ONE_ON_ONE_CHAT_ROOM_SUCCESS(200, "O3CR002", "일대일 채팅방 퇴장 성공"),
	;
	private final int status;
	private final String code;
	private final String message;
}
