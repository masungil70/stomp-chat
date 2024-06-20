<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
  <head>
  	<title>Spring stomp chat</title>
  </head>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.0/js/bootstrap.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.5.0/sockjs.min.js" integrity="sha512-5yJ548VSnLflcRxWNqVWYeQZnby8D8fJTmYRLyvs445j1XmzR8cnWi85lcHx3CUEeAX+GrK3TqTfzOO6LKDpdw==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.2.0/stomp.min.js" integrity="sha512-8gcNcTAF0ZFd2LC6OUiO6UMEOhJ5Zejj1CU8XfCsjk2rDdcDudpn013YuTRWQAugs0nh1ixye6c0przHz3oFRw==" crossorigin="anonymous" referrerpolicy="no-referrer"></script>
	
  <body>
 	<div class="container" id="app">
        <div>
            <h2 id="room-name"></h2>
        </div>
        <div class="input-group">
            <div class="input-group-prepend">
                <label class="input-group-text">내용</label>
            </div>
            <input type="text" class="form-control" name="message" id="message">
            <div class="input-group-append">
                <button class="btn btn-primary" type="button" id="send_message_button">보내기</button>
            </div>
        </div>
        <ul class="list-group" id="message_list"></ul>
    </div>
    <script type="text/javascript">
 		  // websocket & stomp 초기화 
			const sock = new SockJS("<c:url value='/ws-stomp'/>");
    	const ws = Stomp.over(sock);
		  const roomId = localStorage.getItem('chat.roomId');
		  const sender = localStorage.getItem('chat.sender');
		  
		  console.log("ws", ws);
		  console.log("roomId", roomId);
		  console.log("sender", sender);
		  
    	// pub/sub 이벤트 설정 
      ws.connect({}, function(frame) {
    	  //메시지 수신 이벤트 핸들러 등록
				ws.subscribe("<c:url value='/sub/chat/room/${roomId}'/>"
					, message => {
	        	const recv = JSON.parse(message.body);
	          recvMessage(recv);
	      });
    	  
    	  //처음 시작시 채팅방에 입장을 서버에 전송한다
        ws.send("<c:url value='/pub/chat/message'/>", {}
        , JSON.stringify({type:'ENTER', roomId : roomId, sender : sender}));
        }
     	, error => {
      	alert("error " + error);
      });
    	 
    	$("#message").on("keydown", e => {
    		if (e.keyCode == 13) {
    			sendMessage();
    		}
    	});
    	
    	$("#send_message_button").on("click", e => {
    		sendMessage();
    	});
    	
    	
    	$(document).ready(()=>{
    		//채팅방의 정보를 얻는다
    		getRoomInfo();
    	});

    	//roomId를 이용하여 채팅방 정보를 출력한다 
    	//현재는 이름만 출력함  
    	const getRoomInfo = () => {
        	$.ajax({ 
        		type: "GET",
        		url: "<c:url value='/chat/room/'/>" + roomId,
        		dataType: "json" // 요청을 서버로 해서 응답이 왔을 때 기본적으로 모든 것이 문자열 (생긴게 json이라면) => javascript오브젝트로 변경
        	}).done(resp => {
        		//채팅방의 이름을 출력한다
           	$("#room-name").text(resp.name);
        	}).fail(err => {
        		alert(err)
        	}); 
      }

    	//서버에 메시지를 전송한다
      const sendMessage = () => {
    	  const message = $("#message").val();
        ws.send("<c:url value='/pub/chat/message'/>", {}, JSON.stringify({type:'TALK', roomId:roomId, sender:sender, message:message}));
        $("#message").val("");
      };
         
      const recvMessage = recv =>  {
    	  
    	  recv.sender = recv.type == 'ENTER' ? '[알림]' : recv.sender;
    	  
    	  $("#message_list").prepend('<li class="list-group-item" >' + recv.sender + ' - ' + recv.message + '</li>'); 
      }
    </script>
	</body>
</html>