package ko.or.kosa.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom implements Comparable<ChatRoom>{
	
  private String roomId; //채팅방 아이디
  private String name;   //채팅방 이름
  @Builder.Default
  @Getter(AccessLevel.PRIVATE) //해당 변수는 외부에서 접근 할 수 없음  
  private Map<String, String> userMap = new HashMap<>();   //채팅방에 입장한 사용자 세션아이디에 대한 이름  

  public static ChatRoom create(String name) {
      return ChatRoom.builder()
      				.roomId(UUID.randomUUID().toString())
      				.name(name)
      				.build();
  }

	@Override
	public int compareTo(ChatRoom o) {
		if (o == null) return 1;
		return name.compareToIgnoreCase(o.name);
	}
	
	public void addSession(String sessionId, String sender) {
		userMap.put(sessionId, sender);
	}
	
	public void removeSession(String sessionId) {
		userMap.remove(sessionId);
	}
  
	public int getUserCount() {
		return userMap.size();
	}
	
	public boolean isExistSessionId(String sessionId) {
		return userMap.containsKey(sessionId);
	}
	
	public String getSender(String sessionId) {
		return userMap.get(sessionId);
	}
  
}

