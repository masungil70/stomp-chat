package ko.or.kosa.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import ko.or.kosa.dto.ChatMessageDTO;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {
	
	private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
	
  private final SimpMessageSendingOperations messagingTemplate;

  @MessageMapping("/chat/message")
  public void message(ChatMessageDTO message) {
  	
  		System.out.println("서버에서 메시지 수신 message " + message.toString());
  	
      if (ChatMessageDTO.MessageType.ENTER.equals(message.getType())) {
          message.setMessage(message.getSender() + "님이 입장하셨습니다.");
      }
      //서버에서 클라이언트로 구독 메시지를 전달한다 
      messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
  }
}
