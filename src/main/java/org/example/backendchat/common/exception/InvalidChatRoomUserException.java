package org.example.backendchat.common.exception;

import org.example.backendchat.common.error.ErrorCode;

import lombok.Getter;

@Getter
public class InvalidChatRoomUserException extends RuntimeException{

	private final ErrorCode errorCode;

	public InvalidChatRoomUserException(String message, ErrorCode errorCode){
		super(message);
		this.errorCode = errorCode;
	}

}
