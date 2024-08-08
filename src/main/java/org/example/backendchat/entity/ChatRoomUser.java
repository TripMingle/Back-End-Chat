package org.example.backendchat.entity;

import org.example.backendchat.common.entity.BaseEntity;
import org.hibernate.annotations.Where;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "is_deleted = false")
public class ChatRoomUser extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	private Long chatRoomId;

	@Enumerated(value = EnumType.STRING)
	private ChatRoomType chatRoomType;

	private boolean connectionState;

	@Builder
	public ChatRoomUser(User user, Long chatRoomId, ChatRoomType chatRoomType) {
		this.user = user;
		this.chatRoomId = chatRoomId;
		this.chatRoomType = chatRoomType;
	}

	public void changeConnectionState(boolean changeState) {
		this.connectionState = changeState;
	}
}
