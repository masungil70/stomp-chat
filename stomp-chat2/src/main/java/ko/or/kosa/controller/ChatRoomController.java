package ko.or.kosa.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ko.or.kosa.dao.ChatRoomDAO;
import ko.or.kosa.entity.ChatRoom;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChatRoomController {

	private final ChatRoomDAO chatRoomDAO;

	@GetMapping("/roomList")
	public String roomList(Model model) {
		return "/chat/roomList";
	}

	@GetMapping("/rooms")
	@ResponseBody
	public List<ChatRoom> rooms() {
		return chatRoomDAO.findAllRoom();
	}

	@PostMapping("/room")
	@ResponseBody
	public ChatRoom createRoom(@RequestParam String name) {
		return chatRoomDAO.createChatRoom(name);
	}

	@GetMapping("/room/enter/{roomId}")
	public String roomEnter(Model model, @PathVariable String roomId) {
		model.addAttribute("roomId", roomId);
		return "/chat/chatRoom";
	}

	@GetMapping("/room/{roomId}")
	@ResponseBody
	public ChatRoom roomInfo(@PathVariable String roomId) {
		return chatRoomDAO.findRoomById(roomId);
	}
}