package org.example.backendchat.chatroom.repository;

import org.example.backendchat.entity.OneOnOneChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OneOnOneChatRoomRepository extends JpaRepository<OneOnOneChatRoom, Long> {

	boolean existsByUser1IdAndUser2Id(Long user1Id, Long user2Id);

	OneOnOneChatRoom findByUser1IdAndUser2Id(Long user1Id, Long user2Id);
}
