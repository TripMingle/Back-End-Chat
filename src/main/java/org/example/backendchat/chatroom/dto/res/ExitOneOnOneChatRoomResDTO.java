package org.example.backendchat.chatroom.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExitOneOnOneChatRoomResDTO {

    private Long oneOnOneChatRoomId;
    private Long userId;

}
