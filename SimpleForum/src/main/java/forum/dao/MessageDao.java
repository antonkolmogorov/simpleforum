package forum.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import forum.entity.Message;
import forum.entity.Topic;
import forum.entity.User;

@Repository("MessageDao")
public class MessageDao extends EntityDao<Message> {

	@Autowired
	private ValidatorFactory validatorFactory;

	@Transactional(propagation = Propagation.REQUIRED)
	public Message createMessage(String text, int authorId, int topicId) {
		Message message = new Message(text);

		Validator validator = validatorFactory.getValidator();
		Set<ConstraintViolation<Message>> result = validator.validate(message);

		if (result.isEmpty()) {
			Session session = getSession();
			User author = session.get(User.class, authorId);
			if (author != null) {
				message.setAuthor(author);
				author.getEntities().add(message);
				session.saveOrUpdate(author);
			}
			Topic root = session.get(Topic.class, topicId);
			if (root != null) {
				message.setRoot(root);
				root.getEntities().add(message);
				session.saveOrUpdate(root);
			}
			session.save(message);

			return message;
		} else {
			result.forEach(m -> System.out.println(m.getMessage()));
		}
		return null;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Message createMessage(String text, User author, Topic root) {
		int authorId = author == null ? -1 : author.getId();
		int topicId = root == null ? -1 : root.getId();
		return createMessage(text, authorId, topicId);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteMessage(int id) {
		Session session = getSession();
		Message message = session.get(Message.class, id);
		User author = message.getAuthor();
		if (author != null) {
			author.getEntities().remove(message);
		}
		Topic root = message.getRoot();
		if (root != null) {
			root.getEntities().remove(message);
		}
		session.delete(message);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteMessage(Message message) {
		if (message != null) {
			deleteMessage(message.getId());
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Message getMessage(int id) throws HibernateException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		return getObject(id, false);
	}
}