package ko.or.kosa.dto;

import lombok.Data;

@Data
public class ChatMessageDTO {
  // 메시지 타입 : 입장, 채팅
  public enum MessageType {
      ENTER, TALK
  }

  private MessageType type; // 메시지 타입
  private String roomId; // 방번호
  private String sender; // 메시지 보낸사람
  private String message; // 메시지
}
