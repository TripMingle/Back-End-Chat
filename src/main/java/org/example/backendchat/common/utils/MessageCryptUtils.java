package org.example.backendchat.common.utils;

import org.example.backendchat.chat.dto.ChatMessageDTO;
import org.example.backendchat.chat.dto.DecryptedChatMessageDTO;
import org.example.backendchat.chat.dto.EncryptedChatMessageDTO;
import org.example.backendchat.entity.ChatRoomType;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class MessageCryptUtils {

	private final StandardPBEStringEncryptor encryptor;
	private final ObjectMapper objectMapper;

	public MessageCryptUtils(@Value("${jasypt.encryptor.password}") String encryptKey, ObjectMapper objectMapper) {
		this.encryptor = new StandardPBEStringEncryptor();
		this.encryptor.setAlgorithm("PBEWithMD5AndDES");
		this.encryptor.setPassword(encryptKey);
		this.objectMapper = objectMapper;
	}

	public String encrypt(String plainText) {
		return encryptor.encrypt(plainText);
	}

	public String decrypt(String encryptedText) {
		return encryptor.decrypt(encryptedText);
	}

	public EncryptedChatMessageDTO encryptChatMessage(ChatMessageDTO chatMessageDTO) {
		return EncryptedChatMessageDTO.builder()
			.message(encrypt(chatMessageDTO.getMessage()))
			.senderId(encrypt(chatMessageDTO.getSenderId().toString()))
			.chatRoomType(encrypt(chatMessageDTO.getChatRoomType().getType()))
			.chatRoomId(encrypt(chatMessageDTO.getChatRoomId().toString()))
			.build();
	}

	public ChatMessageDTO decryptChatMessage(EncryptedChatMessageDTO encryptedChatMessageDTO) {
		DecryptedChatMessageDTO decryptedChatMessageDTO = DecryptedChatMessageDTO.builder()
			.message(decrypt(encryptedChatMessageDTO.getMessage()))
			.senderId(Long.parseLong(decrypt(encryptedChatMessageDTO.getSenderId())))
			.chatRoomType(ChatRoomType.valueOf(decrypt(encryptedChatMessageDTO.getChatRoomType())))
			.chatRoomId(Long.parseLong(decrypt(encryptedChatMessageDTO.getChatRoomId())))
			.build();
		return objectMapper.convertValue(decryptedChatMessageDTO, ChatMessageDTO.class);
	}

}
