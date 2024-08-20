package org.example.backendchat.chatroom;

import static org.example.backendchat.common.constants.Constants.*;

import java.time.LocalDateTime;
import java.util.List;

import org.example.backendchat.board.BoardRepository;
import org.example.backendchat.chat.ChatMongoRepository;
import org.example.backendchat.chat.dto.ChatMessageDTO;
import org.example.backendchat.chat.dto.EncryptedChatMessageDTO;
import org.example.backendchat.chatroom.dto.etc.ChatRoomChatMessageDTO;
import org.example.backendchat.chatroom.dto.req.EnterGroupChatRoomReqDTO;
import org.example.backendchat.chatroom.dto.req.EnterOneOnOneChatRoomReqDTO;
import org.example.backendchat.chatroom.dto.req.ExitGroupChatRoomReqDTO;
import org.example.backendchat.chatroom.dto.req.ExitOneOnOneChatRoomReqDTO;
import org.example.backendchat.chatroom.dto.res.EnterGroupChatRoomResDTO;
import org.example.backendchat.chatroom.dto.res.EnterOneOnOneChatRoomResDTO;
import org.example.backendchat.chatroom.dto.res.ExitGroupChatRoomResDTO;
import org.example.backendchat.chatroom.dto.res.ExitOneOnOneChatRoomResDTO;
import org.example.backendchat.chatroom.repository.GroupChatRoomRepository;
import org.example.backendchat.chatroom.repository.OneOnOneChatRoomRepository;
import org.example.backendchat.chatroomuser.ChatRoomUserRepository;
import org.example.backendchat.common.error.ErrorCode;
import org.example.backendchat.common.exception.BoardNotFoundException;
import org.example.backendchat.common.exception.ChatRoomNotFoundException;
import org.example.backendchat.common.exception.ChatRoomUserNotFoundException;
import org.example.backendchat.common.exception.UserNotFoundException;
import org.example.backendchat.common.utils.MessageCryptUtils;
import org.example.backendchat.entity.Board;
import org.example.backendchat.entity.ChatMessage;
import org.example.backendchat.entity.ChatRoomType;
import org.example.backendchat.entity.ChatRoomUser;
import org.example.backendchat.entity.GroupChatRoom;
import org.example.backendchat.entity.OneOnOneChatRoom;
import org.example.backendchat.entity.User;
import org.example.backendchat.redis.RedisSubscriber;
import org.example.backendchat.user.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomService {

	private final GroupChatRoomRepository groupChatRoomRepository;
	private final OneOnOneChatRoomRepository oneOnOneChatRoomRepository;
	private final ChatRoomUserRepository chatRoomUserRepository;
	private final UserRepository userRepository;
	private final BoardRepository boardRepository;
	private final ChatMongoRepository chatMongoRepository;
	private final RedisTemplate<String, Object> redisTemplate;
	private final RedisMessageListenerContainer redisMessageListenerContainer;
	private final MessageCryptUtils messageCryptUtils;
	private final ObjectMapper objectMapper;
	private final RedisSubscriber redisSubscriber;

	// 일대일 채팅방 입장
	public EnterOneOnOneChatRoomResDTO enterOneOnOneChatRoom(EnterOneOnOneChatRoomReqDTO enterOneOnOneChatRoomReqDTO) {
		User currentUser = userRepository.findById(enterOneOnOneChatRoomReqDTO.getUserId())
			.orElseThrow(() -> new UserNotFoundException("User not found", ErrorCode.USER_NOT_FOUND));
		User contactUser = userRepository.findById(enterOneOnOneChatRoomReqDTO.getContactUserId())
			.orElseThrow(() -> new UserNotFoundException("Contact user not found", ErrorCode.USER_NOT_FOUND));
		OneOnOneChatRoom oneOnOneChatRoom = saveOrFetchOneOnOneChatRoom(currentUser, contactUser);
		return EnterOneOnOneChatRoomResDTO.builder()
			.chatRoomId(oneOnOneChatRoom.getId())
			.participantId1(currentUser.getId())
			.participantId2(contactUser.getId())
			.build();
	}

	// 그룹 채팅방 입장
	public EnterGroupChatRoomResDTO enterGroupChatRoom(EnterGroupChatRoomReqDTO enterGroupChatRoomReqDTO) {
		User user = userRepository.findById(enterGroupChatRoomReqDTO.getUserId())
			.orElseThrow(() -> new UserNotFoundException("User not found", ErrorCode.USER_NOT_FOUND));
		Board board = boardRepository.findById(enterGroupChatRoomReqDTO.getBoardId())
			.orElseThrow(() -> new BoardNotFoundException("Board not found", ErrorCode.BOARD_NOT_FOUND));
		GroupChatRoom groupChatRoom = saveOrFetchGroupChatRoom(board, board.getUser());
		existStateCheckAndSaveUserInGroupChatRoom(user, groupChatRoom);
		return EnterGroupChatRoomResDTO.builder()
			.groupChatRoomId(groupChatRoom.getId())
			.userId(user.getId())
			.build();
	}

	// 그룹 채팅방 퇴장
	public ExitGroupChatRoomResDTO exitGroupChatRoom(ExitGroupChatRoomReqDTO exitGroupChatRoomReqDTO) {
		GroupChatRoom groupChatRoom = groupChatRoomRepository.findById(exitGroupChatRoomReqDTO.getGroupChatRoomId())
			.orElseThrow(() -> new ChatRoomNotFoundException("Chat Room not found", ErrorCode.CHAT_ROOM_NOT_FOUND));
		User user = userRepository.findById(exitGroupChatRoomReqDTO.getUserId())
			.orElseThrow(() -> new UserNotFoundException("User not found", ErrorCode.USER_NOT_FOUND));
		exitUserForChatRoom(groupChatRoom.getId(), groupChatRoom.getUser().getId(), user.getId());
		deleteGroupChatRoom(groupChatRoom, groupChatRoom.getUser().getId(), user.getId());
		return ExitGroupChatRoomResDTO.builder()
			.groupChatRoomId(groupChatRoom.getId())
			.userId(user.getId())
			.build();
	}

	// 일대일 채팅방 퇴장
	public ExitOneOnOneChatRoomResDTO exitOneOnOneChatRoom(ExitOneOnOneChatRoomReqDTO exitOneOnOneChatRoomReqDTO) {
		User user = userRepository.findById(exitOneOnOneChatRoomReqDTO.getUserId())
			.orElseThrow(() -> new UserNotFoundException("User not found", ErrorCode.USER_NOT_FOUND));
		OneOnOneChatRoom oneOnOneChatRoom = oneOnOneChatRoomRepository.findById(
				exitOneOnOneChatRoomReqDTO.getOneOnOneChatRoomId())
			.orElseThrow(() -> new ChatRoomNotFoundException("Chat Room not found", ErrorCode.CHAT_ROOM_NOT_FOUND));
		ChatRoomUser chatRoomUser = chatRoomUserRepository.findByChatRoomIdAndUserId(
				exitOneOnOneChatRoomReqDTO.getOneOnOneChatRoomId(), user.getId())
			.orElseThrow(() -> new ChatRoomUserNotFoundException("User in Chat Room Not Found",
				ErrorCode.USER_IN_CHAT_ROOM_NOT_FOUND));
		chatRoomUser.exitChatRoom();
		if (chatRoomUserRepository.countByIdAndChatRoomType(exitOneOnOneChatRoomReqDTO.getOneOnOneChatRoomId(),
			ChatRoomType.ONE_ON_ONE) == 0) {
			oneOnOneChatRoom.delete();
		}
		return ExitOneOnOneChatRoomResDTO.builder()
			.oneOnOneChatRoomId(chatRoomUser.getChatRoomId())
			.userId(chatRoomUser.getUser().getId())
			.build();
	}

	private OneOnOneChatRoom saveOrFetchOneOnOneChatRoom(User currentUser, User contactUser) {
		if (!oneOnOneChatRoomRepository.existsByUser1IdAndUser2Id(currentUser.getId(), contactUser.getId())) {
			OneOnOneChatRoom oneOnOneChatRoom = OneOnOneChatRoom.builder()
				.user1(currentUser)
				.user2(contactUser)
				.build();
			OneOnOneChatRoom savedOneOnOneChatRoom = oneOnOneChatRoomRepository.save(oneOnOneChatRoom);
			ChatRoomUser currentChatRoomUser = generateChatRoomUserEntity(savedOneOnOneChatRoom.getId(),
				currentUser.getId(), ChatRoomType.ONE_ON_ONE, FIRST_ENTER_CHAT_ROOM_CHAT_COUNT);
			chatRoomUserRepository.save(currentChatRoomUser);

			ChatRoomUser contactChatRoomUser = generateChatRoomUserEntity(savedOneOnOneChatRoom.getId(),
				contactUser.getId(), ChatRoomType.ONE_ON_ONE, FIRST_ENTER_CHAT_ROOM_CHAT_COUNT);
			chatRoomUserRepository.save(contactChatRoomUser);
			return savedOneOnOneChatRoom;
		} else {
			return oneOnOneChatRoomRepository.findByUser1IdAndUser2Id(currentUser.getId(), contactUser.getId());
		}
	}

	private GroupChatRoom saveOrFetchGroupChatRoom(Board board, User masterUser) {
		if (!groupChatRoomRepository.existsByBoardId(board.getId())) {
			GroupChatRoom newChatRoom = GroupChatRoom.builder()
				.board(board)
				.user(masterUser)
				.build();
			GroupChatRoom savedGroupChatRoom = groupChatRoomRepository.save(newChatRoom);
			ChatRoomUser chatRoomMasterUser = ChatRoomUser.builder()
				.user(board.getUser())
				.chatRoomId(savedGroupChatRoom.getId())
				.chatRoomType(ChatRoomType.GROUP)
				.chatFirstIndex(FIRST_ENTER_CHAT_ROOM_CHAT_COUNT)
				.build();
			chatRoomUserRepository.save(chatRoomMasterUser);
			return savedGroupChatRoom;
		} else {
			return groupChatRoomRepository.findByBoardId(board.getId())
				.orElseThrow(() -> new BoardNotFoundException("Board not found", ErrorCode.BOARD_NOT_FOUND));
		}
	}

	private void existStateCheckAndSaveUserInGroupChatRoom(User user, GroupChatRoom groupChatRoom) {
		if (!chatRoomUserRepository.existsByUserIdAndChatRoomIdAndChatRoomType(user.getId(), groupChatRoom.getId(),
			ChatRoomType.GROUP)) {
			Long alreadyExistsChatCount = chatMongoRepository.countByChatRoomIdAndChatRoomType(groupChatRoom.getId(),
				ChatRoomType.GROUP);
			ChatRoomUser chatRoomUser = ChatRoomUser.builder()
				.chatRoomId(groupChatRoom.getId())
				.user(user)
				.chatRoomType(ChatRoomType.GROUP)
				.chatFirstIndex(alreadyExistsChatCount)
				.build();
			chatRoomUserRepository.save(chatRoomUser);
			sendEnterOrExitMessage(groupChatRoom.getId(), user, true);
		}
	}

	private ChatRoomUser generateChatRoomUserEntity(Long chatRoomId, Long userId, ChatRoomType chatRoomType,
		Long chatFirstIndex) {
		return ChatRoomUser.builder()
			.chatRoomId(chatRoomId)
			.user(userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found", ErrorCode.USER_NOT_FOUND)))
			.chatRoomType(chatRoomType)
			.chatFirstIndex(chatFirstIndex)
			.build();
	}

	private void exitUserForChatRoom(Long chatRoomId, Long chatRoomMasterUserId, Long userId) {
		if (chatRoomMasterUserId.equals(userId)) {
			List<ChatRoomUser> chatRoomUsers = chatRoomUserRepository.findAllByChatRoomIdAndUserId(chatRoomId,
				chatRoomMasterUserId);
			chatRoomUsers.forEach(ChatRoomUser::exitChatRoom);
		} else {
			ChatRoomUser chatRoomUser = chatRoomUserRepository.findByChatRoomIdAndUserId(chatRoomId, userId)
				.orElseThrow(() -> new ChatRoomUserNotFoundException("User in Chat Room Not Found",
					ErrorCode.USER_IN_CHAT_ROOM_NOT_FOUND));
			chatRoomUser.exitChatRoom();
			sendEnterOrExitMessage(chatRoomId, chatRoomUser.getUser(), false);
		}
	}

	private void deleteGroupChatRoom(GroupChatRoom groupChatRoom, Long chatRoomMasterId, Long userId) {
		if (chatRoomMasterId.equals(userId)) {
			groupChatRoom.delete();
		}
	}

	private void sendEnterOrExitMessage(Long chatRoomId, User user, boolean isEnter) {
		ChannelTopic channelTopic = new ChannelTopic(ChatRoomType.GROUP.getType() + "/" + chatRoomId.toString());
		redisMessageListenerContainer.addMessageListener(redisSubscriber, channelTopic);
		ChatRoomChatMessageDTO chatRoomChatMessageDTO = ChatRoomChatMessageDTO.builder()
			.chatRoomId(chatRoomId)
			.chatRoomType(ChatRoomType.GROUP)
			.senderId(user.getId())
			.message(isEnter ? user.getNickName() + "님이 입장하셨습니다." : user.getNickName() + "님이 퇴장하셨습니다.")
			.build();
		ChatMessageDTO chatMessageDTO = objectMapper.convertValue(chatRoomChatMessageDTO, ChatMessageDTO.class);
		saveChatMessageWithAsync(chatMessageDTO);
		EncryptedChatMessageDTO encryptedChatMessageDTO = messageCryptUtils.encryptChatMessage(chatMessageDTO);
		redisTemplate.convertAndSend(channelTopic.getTopic(), encryptedChatMessageDTO);
	}

	@Async
	public void saveChatMessageWithAsync(ChatMessageDTO chatMessageDTO) {
		ChatMessage chatMessage = ChatMessage.builder()
			.message(chatMessageDTO.getMessage())
			.senderId(chatMessageDTO.getSenderId())
			.chatRoomType(chatMessageDTO.getChatRoomType())
			.chatRoomId(chatMessageDTO.getChatRoomId())
			.sendingTime(LocalDateTime.now().toString())
			.build();
		chatMongoRepository.save(chatMessage);
	}

}
