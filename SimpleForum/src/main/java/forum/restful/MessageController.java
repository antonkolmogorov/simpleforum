package forum.restful;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import forum.dao.MessageDao;
import forum.entity.Message;

@RestController
@RequestMapping(MessageController.MAPPING)
public class MessageController {
	
	public static final String COMPOSE = "/compose";
	public static final String DELETE = "/delete";
	public static final String MAPPING = "/message";

	@Autowired
	private MessageDao dao;

	@RequestMapping(COMPOSE)
	public Message compose(@RequestParam(name = "text", required = true) String text,
			@RequestParam(name = "author", required = true) int authorId,
			@RequestParam(name = "topic", required = true) int topicId) {
		return dao.createMessage(text, authorId, topicId);
	}
	
	@RequestMapping(DELETE)
	public void delete(@RequestParam(name = "id", required = true) int id) {
		dao.deleteMessage(id);
	}
}