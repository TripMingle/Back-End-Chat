package org.example.backendchat.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ChatRoomType {

	GROUP("GROUP"), ONE_ON_ONE("ONE_ON_ONE");

	private final String type;
}
