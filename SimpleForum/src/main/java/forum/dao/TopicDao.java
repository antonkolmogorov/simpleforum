package forum.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import forum.entity.ForumEntity;
import forum.entity.Topic;
import forum.entity.User;

@Repository("TopicDao")
public class TopicDao extends EntityDao<Topic> {

	@Autowired
	private ValidatorFactory validatorFactory;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public Topic createTopic(String title, int authorId, int topicId) {
		Topic topic = new Topic(title);
		
		Validator validator = validatorFactory.getValidator();
		Set<ConstraintViolation<Topic>> result = validator.validate(topic);
		
		if (result.isEmpty()) {
			Session session = getSession();
			
			User author = session.get(User.class, authorId);
			if (author != null) {
				topic.setAuthor(author);
				author.getEntities().add(topic);
				session.saveOrUpdate(author);
			}
			
			Topic root = session.get(Topic.class, topicId);
			if (root != null) {
				topic.setRoot(root);
				root.getEntities().add(topic);
				session.saveOrUpdate(root);
			}
			
			session.save(topic);
			
			return topic;
		} else {
			result.forEach(m -> System.out.println(m.getMessage()));
		}
		return null;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Topic createTopic(String name, User author, Topic root) {
		int authorId = author == null ? -1 : author.getId();
		int topicId = root == null ? -1 : root.getId();
		return createTopic(name, authorId, topicId);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteTopic(int id) {
		deleteRoutine(id, true, getSession());
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteTopic(Topic topic) {
		if (topic != null) {
			deleteTopic(topic.getId());
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<Topic> getRoots() {
		Session session = getSession();

		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Topic> query = builder.createQuery(Topic.class);

		Root<Topic> root = query.from(Topic.class);
		query.select(root).where(builder.isNull(root.get("root")));
		List<Topic> results = session.createQuery(query).getResultList();

		return results;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Topic getTopic(int id) throws HibernateException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		return getObject(id, true);
	}

	private void deleteRoutine(int id, boolean isRoot, Session session) {
		Topic topic = session.get(Topic.class, id);
		if (topic != null) {
			// remove from the author
			User author = topic.getAuthor();
			if (author != null) {
				author.getEntities().remove(topic);
			}

			// remove from the root topic
			if (isRoot) {
				Topic root = topic.getRoot();
				if (root != null) {
					root.getEntities().remove(topic);
				}
			}

			// remove all the contained topics
			Iterator<ForumEntity> iterator = topic.getEntities().iterator();
			while (iterator.hasNext()) {
				deleteRoutine(iterator.next().getId(), false, session);
			}

			// remove all the contained messages
			for (ForumEntity entity : topic.getEntities()) {
				User messageAuthor = entity.getAuthor();
				if (messageAuthor != null) {
					messageAuthor.getEntities().remove(entity);
				}
				session.delete(entity);
			}
			topic.getEntities().clear();

			// finally delete the topic
			session.delete(topic);
		}
	}
}