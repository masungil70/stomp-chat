package ko.or.kosa.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import ko.or.kosa.entity.ChatRoom;

@Repository
public class ChatRoomDAO {
	private final String subChatRoomPath = "/sub/chat/room/";
  private Map<String, ChatRoom> chatRoomMap;

  @PostConstruct
  private void init() {
      chatRoomMap = new LinkedHashMap<>();
  }

  public List<ChatRoom> findAllRoom() {
    // 채팅방 이름 순으로 반환
		return chatRoomMap.values().stream().sorted().collect(Collectors.toList());
  }

  public ChatRoom findRoomById(String id) {
      return chatRoomMap.get(id);
  }

  public ChatRoom createChatRoom(String name) {
      ChatRoom chatRoom = ChatRoom.create(name);
      chatRoomMap.put(chatRoom.getRoomId(), chatRoom);
      return chatRoom;
  }

  //사용자가 채팅방에 입장한다
	public ChatRoom userEnterChatRoomUser(String path, String sessionId, String sender) {
		String newRoomId = path.substring(subChatRoomPath.length());
		ChatRoom chatRoom = chatRoomMap.get(newRoomId);
		if (chatRoom != null) {
			chatRoom.addSession(sessionId, sender);
		}
		return chatRoom;
	}

  public ChatRoom findRoomSessionId(String sessionId) {
		for (Entry<String, ChatRoom> entry : chatRoomMap.entrySet()) {
			if (entry.getValue().isExistSessionId(sessionId)) {
				return entry.getValue();
			}
		}
		return null;
  }
  
  //사용자가 채팅방에서 퇴장한다
	public void userLeaveChatRoomUser(String sessionId) {
		ChatRoom chatRoom = findRoomSessionId(sessionId);
		if (chatRoom != null) {
			chatRoom.removeSession(sessionId);
		}
	}

}
