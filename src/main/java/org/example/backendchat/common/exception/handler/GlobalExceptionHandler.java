package org.example.backendchat.common.exception.handler;

import org.example.backendchat.common.error.ErrorResponse;
import org.example.backendchat.common.exception.BoardNotFoundException;
import org.example.backendchat.common.exception.ChatMessageSendingException;
import org.example.backendchat.common.exception.ChatRoomNotFoundException;
import org.example.backendchat.common.exception.InvalidAccessTokenException;
import org.example.backendchat.common.exception.InvalidGroupChatRoomException;
import org.example.backendchat.common.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
		log.error("handleUserNotFoundException", ex);
		final ErrorResponse response = new ErrorResponse(ex.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
	}

	@ExceptionHandler(BoardNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleBoardNotFoundException(BoardNotFoundException ex) {
		log.error("handleBoardNotFoundException", ex);
		final ErrorResponse response = new ErrorResponse(ex.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
	}

	@ExceptionHandler(InvalidGroupChatRoomException.class)
	public ResponseEntity<ErrorResponse> handleInvalidGroupChatRoomException(InvalidGroupChatRoomException ex) {
		log.error("handleInvalidGroupChatRoomException", ex);
		final ErrorResponse response = new ErrorResponse(ex.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
	}

	@ExceptionHandler(ChatRoomNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleChatRoomNotFoundException(ChatRoomNotFoundException ex) {
		log.error("handleChatRoomNotFoundException", ex);
		final ErrorResponse response = new ErrorResponse(ex.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
	}

	@ExceptionHandler(ChatMessageSendingException.class)
	public ResponseEntity<ErrorResponse> handleChatMessageSendingException(ChatMessageSendingException ex) {
		log.error("handleChatMessageSendingException", ex);
		final ErrorResponse response = new ErrorResponse(ex.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
	}

	@ExceptionHandler(InvalidAccessTokenException.class)
	public ResponseEntity<ErrorResponse> handleInvalidAccessTokenException(InvalidAccessTokenException ex) {
		log.error("handleInvalidAccessTokenException", ex);
		final ErrorResponse response = new ErrorResponse(ex.getErrorCode());
		return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getErrorCode().getStatus()));
	}

}
