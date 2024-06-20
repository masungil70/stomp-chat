package ko.or.kosa.service;

import java.util.List;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import ko.or.kosa.dao.ChatRoomDAO;
import ko.or.kosa.dto.ChatMessageDTO;
import ko.or.kosa.entity.ChatRoom;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
	private final ChatRoomDAO chatRoomDAO;
  private final SimpMessageSendingOperations messagingTemplate;
	

	public List<ChatRoom> findAllRoom() {
		return chatRoomDAO.findAllRoom();
	}

	public ChatRoom createChatRoom(String name) {
		return chatRoomDAO.createChatRoom(name);
	}

	public ChatRoom findRoomById(String roomId) {
		return chatRoomDAO.findRoomById(roomId);
	}

	public void userEnterChatRoomUser(String simpDestination, String sessionId, String sender) {
		ChatRoom chatRoom = chatRoomDAO.userEnterChatRoomUser(simpDestination, sessionId, sender);
		if (chatRoom != null) {
	    //서버에서 클라이언트로 구독 메시지를 전달한다 
			//채팅방의 입장한 모든 사용자에게 입장으로 알린다
	    messagingTemplate.convertAndSend("/sub/chat/room/" + chatRoom.getRoomId()
	    	, ChatMessageDTO.enterMessage(chatRoom, sender));
		}
	}

	public void userLeaveChatRoomUser(String sessionId) {
		ChatRoom chatRoom = chatRoomDAO.findRoomSessionId(sessionId);
		if (chatRoom != null) {
			String sender = chatRoom.getSender(sessionId);
			System.out.println("userLeaveChatRoomUser() sender : " + sender);
			chatRoomDAO.userLeaveChatRoomUser(sessionId);
	    //서버에서 클라이언트로 구독 메시지를 전달한다 
			//채팅방의 입장한 모든 사용자에게 퇴장으로 알린다
	    messagingTemplate.convertAndSend("/sub/chat/room/" + chatRoom.getRoomId()
	    	, ChatMessageDTO.leaveMessage(chatRoom, sender));
		}
	}

}
