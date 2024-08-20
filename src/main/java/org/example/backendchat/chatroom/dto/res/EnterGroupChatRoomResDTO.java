package org.example.backendchat.chatroom.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EnterGroupChatRoomResDTO {

    private Long groupChatRoomId;
    private Long userId;

}
