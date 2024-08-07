package org.example.backendchat.entity;

import org.example.backendchat.common.entity.BaseEntity;
import org.hibernate.annotations.Where;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "is_deleted = false")
public class GroupChatRoom extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "board_id", nullable = false)
	private Board board;

	@OneToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Builder
	public GroupChatRoom(Board board, User user) {
		this.board = board;
		this.user = user;
	}
}
