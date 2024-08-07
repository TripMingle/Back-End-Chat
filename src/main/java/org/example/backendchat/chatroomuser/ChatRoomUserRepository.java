package org.example.backendchat.chatroomuser;

import org.example.backendchat.entity.ChatRoomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {

	int countByChatRoomId(Long boardId);
}
