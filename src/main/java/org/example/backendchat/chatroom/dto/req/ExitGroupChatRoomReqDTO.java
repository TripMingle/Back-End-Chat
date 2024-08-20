package org.example.backendchat.chatroom.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExitGroupChatRoomReqDTO {

    private Long userId;
    private Long groupChatRoomId;

}
