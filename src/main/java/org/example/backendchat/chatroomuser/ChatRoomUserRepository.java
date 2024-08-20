package org.example.backendchat.chatroomuser;

import java.util.List;
import java.util.Optional;

import org.example.backendchat.entity.ChatRoomType;
import org.example.backendchat.entity.ChatRoomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {

	Optional<ChatRoomUser> findByChatRoomIdAndChatRoomTypeAndUserId(Long chatRoomId, ChatRoomType chatRoomType,
		Long userId);

	int countByIdAndChatRoomType(Long groupChatRoomId, ChatRoomType chatRoomType);

	List<ChatRoomUser> findAllByChatRoomIdAndUserId(Long chatRoomId, Long chatRoomMasterUserId);

	Optional<ChatRoomUser> findByChatRoomIdAndUserId(Long chatRoomId, Long userId);

	boolean existsByUserIdAndChatRoomIdAndChatRoomType(Long userId, Long chatRoomId, ChatRoomType chatRoomType);

}
