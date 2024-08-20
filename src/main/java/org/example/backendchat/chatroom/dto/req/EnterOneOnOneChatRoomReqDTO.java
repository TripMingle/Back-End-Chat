package org.example.backendchat.chatroom.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnterOneOnOneChatRoomReqDTO {

    private Long userId;
    private Long contactUserId;

}
