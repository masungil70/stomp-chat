package ko.or.kosa.config;

import java.util.Map.Entry;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/*
 * Stomp에 이해 발생되는 모든 상황에 대한 리스너를 의미한다 
 * 상황 : 
 * 	메시지 전송전,
 *  메시지 전송후,
 *  메시지 전송 완료 후
 *   
 * 	메시지 수신전,
 *  메시지 수신후,
 *  메시지 수신 완료 후
 *  
 * 인증관련 작업을 하려고 한다면 해당 클래스 메시지 전송전과 메시지 수신전에 코드를 추가하면 된다
 *  
 */

@Slf4j
@Component
public class StompHandler implements ChannelInterceptor {
    // websocket을 통해 들어온 요청이 처리 되기전 실행된다.
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        for (Entry<String, Object> entry : message.getHeaders().entrySet()) {
          log.info("preSend() header -> {}", entry); 
        }
        if (StompCommand.CONNECT == accessor.getCommand()) { // websocket 연결요청
            String jwtToken = accessor.getFirstNativeHeader("token");
            log.info("preSend() CONNECT message = {}", message);
            log.info("preSend() CONNECT channel = {}", channel);
            log.info("preSend() CONNECT accessor = {}", accessor);
            log.info("preSend() CONNECT jwtToken = {}", jwtToken);
        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) { // 채팅룸 구독요청
            // header정보에서 구독 destination정보를 얻고, roomId를 추출한다.
            for (Entry<String, Object> entry : message.getHeaders().entrySet()) {
              log.info("preSend() StompCommand.SUBSCRIBE {}", entry); 
            }
        } else if (StompCommand.DISCONNECT == accessor.getCommand()) { // Websocket 연결 종료
            // 연결이 종료된 클라이언트 sesssionId로 채팅방 id를 얻는다.
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            log.info("preSend() DISCONNECTED sessionId = {}", sessionId);
            log.info("preSend() DISCONNECTED accessor = {}", accessor);
        }
        return message;
    }

		@Override
		public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
      StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
      for (Entry<String, Object> entry : message.getHeaders().entrySet()) {
        log.info("postSend() header -> {}", entry); 
      }
      log.info("postSend() message -> {}", message); 
      log.info("postSend() channel -> {}", channel); 
      log.info("postSend() sent -> {}", sent); 
      log.info("postSend() accessor -> {}", accessor); 
			
		}

		@Override
		public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
      StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
      for (Entry<String, Object> entry : message.getHeaders().entrySet()) {
        log.info("afterSendCompletion() header -> {}", entry); 
      }
      log.info("afterSendCompletion() message -> {}", message); 
      log.info("afterSendCompletion() channel -> {}", channel); 
      log.info("afterSendCompletion() sent -> {}", sent); 
      log.info("afterSendCompletion() accessor -> {}", accessor); 
			
		}

		@Override
		public boolean preReceive(MessageChannel channel) {
      log.info("preReceive() channel -> {}", channel); 
      
			return false;
		}

		@Override
		public Message<?> postReceive(Message<?> message, MessageChannel channel) {
      StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
      for (Entry<String, Object> entry : message.getHeaders().entrySet()) {
        log.info("postReceive() header -> {}", entry); 
      }
      log.info("postReceive() message -> {}", message); 
      log.info("postReceive() channel -> {}", channel); 
      log.info("postReceive() accessor -> {}", accessor); 
			
      return null;
		}

		@Override
		public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
      StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
      for (Entry<String, Object> entry : message.getHeaders().entrySet()) {
        log.info("afterReceiveCompletion() header -> {}", entry); 
      }
      log.info("afterReceiveCompletion() message -> {}", message); 
      log.info("afterReceiveCompletion() channel -> {}", channel); 
      log.info("afterReceiveCompletion() accessor -> {}", accessor); 			
		}
}