package forum.restful;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import forum.dao.TopicDao;
import forum.entity.ForumEntity;
import forum.entity.Topic;

@RestController
@RequestMapping(TopicController.MAPPING)
public class TopicController {

	public static final String COMPOSE = "/compose";
	public static final String DELETE = "/delete";
	public static final String GET = "/get";
	public static final String MAPPING = "/topic";
	public static final String ROOTS = "/roots";

	@Autowired
	private TopicDao dao;

	@RequestMapping(COMPOSE)
	public Topic compose(@RequestParam(name = "title", required = true) String title,
			@RequestParam(name = "author", required = true) int authorId,
			@RequestParam(name = "topic", required = false, defaultValue = "0") int topicId) {
		return dao.createTopic(title, authorId, topicId);
	}

	@RequestMapping(DELETE)
	public void delete(@RequestParam(name = "id", required = true) int id) {
		dao.deleteTopic(id);
	}

	@RequestMapping(GET)
	public Set<ForumEntity> getTopic(@RequestParam(name = "id", required = true) int id)
			throws HibernateException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		Topic topic = dao.getTopic(id);
		if (topic == null) {
			return null;
		}
		return topic.getEntities();
	}

	@RequestMapping(ROOTS)
	public List<Topic> roots() {
		return dao.getRoots();
	}

}