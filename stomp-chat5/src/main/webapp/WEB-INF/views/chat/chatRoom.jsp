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
            <h2 id="room-info">
            	<span id="name"></span>
            	(<span id="userCount"></span>)
            	<span id="sender"></span>
            </h2>
                <button class="btn btn-primary" type="button" id="char_room_list_button">목록</button>
        </div>
        <div class="input-group">
            <div class="input-group-prepend">
                <label class="input-group-text">내용</label>
            </div>
            <input type="text" class="form-control" name="message" id="message">
            <div class="input-group-append">
                <button class="btn btn-primary" type="button" id="send_message_button">보내기</button>
                <button class="btn btn-primary" type="button" id="leave_button">잠시나가기</button>
                <button class="btn btn-primary disabled" type="button" id="reenter_button">다시입장</button>
            </div>
        </div>
        <div>잠시 나가기는 채팅방에서 수신되는 메시지를 받지 않고 채팅방에 참여한 모든 사람에 메시지는 보낼수 있다</div>
        <ul class="list-group" id="message_list"></ul>
    </div>
    <script type="text/javascript">
 		  // websocket & stomp 초기화 
        const url = "${pageContext.request.contextPath}/ws-stomp";
        const sock = new SockJS(url);
        const ws = Stomp.over(sock);
        const roomId = localStorage.getItem('chat.roomId');
        const sender = localStorage.getItem('chat.sender');
        const token = localStorage.getItem("jwt_token");
		  
        let subscription = null;
		  
        console.log("ws", ws);
        console.log("roomId", roomId);
        console.log("sender", sender);
        console.log("jwt_token", token);
	    
    	// pub/sub 이벤트 설정 
        ws.connect({
        	  Authorization : "Bearer " + token  // <---- 여기에 넣으면 서버의 StompHandler에서 읽을 수 있음
            }, function(frame) {
    	    //메시지 수신 이벤트 핸들러 등록
                subscribe();
	    	  //채팅방에 입장 메시지를 서버에 전송한다
	    	  //enterSendMessage();
            }, error => {
      	       alert("error " + error);
      	});
    	 
    	$("#message").on("keydown", e => {
    		if (e.keyCode == 13) {
    			sendMessage();
    		}
    	});
    	
    	$("#char_room_list_button").on("click", e => {
    		location = "<c:url value='/chat/roomList'/>";
    	});
    	
    	$("#send_message_button").on("click", e => {
    		sendMessage();
    	});
    	
    	$("#leave_button").on("click", e => {
    		$("#leave_button,#send_message_button").addClass("disabled");
    		$("#reenter_button").removeClass("disabled");
   			//잠시 나간다 . 메시지 수신을 하는 않는다
   			unsubscribe();
   			
     	  //채팅방에 퇴장메시지를 서버에 전송한다
     	 //leaveSendMessage();

    	});
    	
    	$("#reenter_button").on("click", e => {
    		$("#reenter_button").addClass("disabled");
    		$("#leave_button,#send_message_button").removeClass("disabled");
    		//채팅방에 입장한다
    		//구독을 신청한다 
				subscribe();
    		
    	  //채팅방에 입장 메시지를 서버에 전송한다
    	  //enterSendMessage();

    	});

    	
    	$(document).ready(()=>{
    		//채팅방의 정보를 얻는다
				console.log("before getRoomInfo");    		
    		getRoomInfo();
    		
    		$(window).on("beforeunload", e => {
    			//구독을 해제 한다
    			unsubscribe();
    			ws.disconnect(() => {
    				console.log("stomp 연결 종료")
    			}, {});
    		})
    	});

  	  //메시지 수신 이벤트 핸들러 등록
  	  //구독을 등록한다
    	const subscribe = () => {
    		subscription = ws.subscribe("<c:url value='/sub/chat/room/${roomId}'/>"
					, message => {
	        	const recv = JSON.parse(message.body);
	          recvMessage(recv);
	      }, {sender:sender});    		
    	}
    	
   	  //메시지 수신를 해제 한다 
   	  //구독을 해제한다
     	const unsubscribe = () => {
     		console.log("subscription", subscription);
     		
     		if(subscription != null) {
     			subscription.unsubscribe();
     			subscription = null;
     		}
     	}
    	
    	//roomId를 이용하여 채팅방 정보를 출력한다 
    	//현재는 이름만 출력함  
    	const getRoomInfo = () => {
        	$.ajax({ 
        		type: "GET",
        		url: "<c:url value='/chat/room/'/>" + roomId,
        		dataType: "json" // 요청을 서버로 해서 응답이 왔을 때 기본적으로 모든 것이 문자열 (생긴게 json이라면) => javascript오브젝트로 변경
        	}).done(chatRoom => {
        		//채팅방의 이름을 출력한다 
						console.log("getRoomInfo ", chatRoom);    		
           	$("#room-info > #name").text(chatRoom.name);
           	$("#room-info > #userCount").text(chatRoom.userCount);
           	$("#room-info > #sender").text(sender);
        	}).fail(err => {
						console.log("getRoomInfo error", err);    		
        	}); 
      }
    	
    	//입장메시지를 전송한다
    	const enterSendMessage = () => {
    		console.log("enterSendMessage ")
     	  //채팅방에 입장을 서버에 전송한다
        ws.send("<c:url value='/pub/chat/message'/>", {}
        , JSON.stringify({type:'ENTER', roomId : roomId, sender : sender}));
      }
    	
    	//퇴장메시지를 전송한다
    	const leaveSendMessage = () => {
     	  //채팅방에 입장을 서버에 전송한다
        ws.send("<c:url value='/pub/chat/message'/>", {}
        , JSON.stringify({type:'LEAVE', roomId : roomId, sender : sender}));
      }
    	
			//채팅방에 입장한 사용자수  
			const displayEnterUserCount = userCount => {
    		$("#userCount").text(userCount);
			}

    	
    	//서버에 메시지를 전송한다
      const sendMessage = () => {
    	  if (subscription == null) return;
    	  const message = $("#message").val().trim();
    	  const url = "${pageContext.request.contextPath}/pub/chat/message";
    	  const payload = JSON.stringify({type:'TALK', roomId:roomId, sender:sender, message:encodeURIComponent(message)}) 
        ws.send(url, {}, payload);
        $("#message").val("");
      };
         
      const recvMessage = recv =>  {
    	  
    	  console.log(recv);
    	  //recv.sender = recv.type == 'ENTER' ? '[알림]' : recv.sender;
    	  //서버에서 처리할 수 있게 개능 개선함 
    	  if (recv.type == 'ENTER' || recv.type == 'LEAVE') {
    		  displayEnterUserCount(recv.userCount);    		  
    	  }  
    	  $("#message_list").prepend('<li class="list-group-item" >' + recv.sender + ' - ' + decodeURIComponent(recv.message) + '</li>'); 
      }
    </script>
	</body>
</html>