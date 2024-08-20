package org.example.backendchat.chatroom.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EnterOneOnOneChatRoomResDTO {

    private Long chatRoomId;
    private Long participantId1;
    private Long participantId2;

}
