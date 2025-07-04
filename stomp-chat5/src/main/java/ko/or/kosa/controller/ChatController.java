package ko.or.kosa.controller;

import java.security.Principal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import ko.or.kosa.dto.ChatMessageDTO;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {
	
	private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
	
  private final SimpMessageSendingOperations messagingTemplate;

  @MessageMapping("/chat/message")
  public void message(ChatMessageDTO message
		  //아래 코드는 세션을 바활성화 한 경우 인증정보를 얻는 방법 
		  , @Header("simpSessionAttributes") Map<String, Object> sessionAttributes
		  //아래 코드는 세션을 활성화 한 경우 인증정보를 얻는 방법 
		  //, @Header("simpUser") Principal principal 
		  ) {
  	
  		System.out.println("서버에서 메시지 수신 message " + message.toString());
  		System.out.println(" simpSessionAttributes " + sessionAttributes);
  		System.out.println(" email " + sessionAttributes.get("email"));
  		
  		//메시지 종류에 따른 처리 
  		message.typeProcess();
  		
      //서버에서 클라이언트로 구독 메시지를 전달한다 
  		//채팅방의 모든 메시지에 대한 로그를 기록하려고 하면 이부분에서 기록하면 된다
      messagingTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
  }
}
