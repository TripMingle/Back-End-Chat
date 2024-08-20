package org.example.backendchat.chatroom.repository;

import java.util.Optional;

import org.example.backendchat.entity.GroupChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupChatRoomRepository extends JpaRepository<GroupChatRoom, Long> {
	boolean existsByBoardId(Long boardId);

	boolean existsByBoardIdAndUserId(Long id, Long id1);

	Optional<GroupChatRoom> findByBoardId(Long boardId);
}
