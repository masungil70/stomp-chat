package ko.or.kosa.dto;

import lombok.Data;

@Data
public class AlarmMessageDTO {
  // 알람 타입 : 사용자 정보 변경 
  public enum MessageType {
      USER_INFO_UPDATE, CHAT_INFO_UPDATE
  }

  private String target; // 대상(변경 대상자 또는 채팅방 변경)  
  	
}
