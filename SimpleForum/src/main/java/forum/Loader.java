package forum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Lazy;

import forum.dao.MessageDao;
import forum.dao.TopicDao;
import forum.dao.UserDao;
import forum.entity.Topic;
import forum.entity.User;

//@Component
public class Loader implements ApplicationRunner {
	
	@Autowired
	private TopicDao topicDao;
	@Autowired
	private MessageDao messageDao;
	@Lazy
	@Autowired
	private UserDao userDao;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		User user = userDao.getUserByName("Onotole");//(1, true);
		
		for (int i = 0; i < 10; i++) {
			userDao.createUser("User" + i, "a@b.c", "pwd" + i);
		}
		
		Topic root = topicDao.createTopic("Root topic", user, null);
		for (int i = 0; i < 10; i++) {
			Topic t = topicDao.createTopic("Child topic " + i, user, root);
			for (int j = 0; j < 10; j++) {
				messageDao.createMessage("Message " + i + " " + j, user, t);
			}
		}
//		List<Topic> roots = topicDao.getRoots();
//		roots.forEach(t -> topicDao.deleteTopic(t));
	}
}