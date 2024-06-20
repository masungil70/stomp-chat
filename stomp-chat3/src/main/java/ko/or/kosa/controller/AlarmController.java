package ko.or.kosa.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import ko.or.kosa.dto.ChatMessageDTO;
import lombok.RequiredArgsConstructor;

/*
 * 알람 관련 컨트롤러 
 */

@Controller
@RequiredArgsConstructor
public class AlarmController {
	
	private static final Logger logger = LoggerFactory.getLogger(AlarmController.class);
	
  private final SimpMessageSendingOperations messagingTemplate;

  @MessageMapping("/alarm/message")
  public void message(ChatMessageDTO message) {
  	
  		System.out.println("서버에서 메시지 수신 message " + message.toString());
  		
  		//메시지 종류에 따른 처리 
  		message.typeProcess();
  		
      //서버에서 클라이언트로 구독 메시지를 전달한다 
  		//채팅방의 모든 메시지에 대한 로그를 기록하려고 하면 이부분에서 기록하면 된다
      messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
  }
}
