package org.example.backendchat.chatroom;

import org.example.backendchat.entity.OneOnOneChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OneOnOneChatRoomRepository extends JpaRepository<OneOnOneChatRoom, Long> {

}
