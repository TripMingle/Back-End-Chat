package org.example.backendchat.chatroomuser;

import java.util.Optional;

import org.example.backendchat.entity.ChatRoomType;
import org.example.backendchat.entity.ChatRoomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {

	Optional<ChatRoomUser> findByChatRoomIdAndChatRoomTypeAndUserId(Long chatRoomId, ChatRoomType chatRoomType, Long userId);
}
