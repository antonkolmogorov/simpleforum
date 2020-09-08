package forum.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import forum.entity.User;

@Repository("UserDao")
public class UserDao extends EntityDao<User> {

	@Autowired
	private ValidatorFactory validatorFactory;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Transactional(propagation = Propagation.REQUIRED)
	public User createUser(String name, String email, String password) {
		String pwdHash = passwordEncoder.encode(password);
		User user = new User(name, email, pwdHash);
		Validator validator = validatorFactory.getValidator();
		Set<ConstraintViolation<User>> result = validator.validate(user);
		if (!result.isEmpty()) {
			System.out.println("Couldn't create a new user:");
			result.forEach(m -> System.out.println(m.getMessage()));
			return null;
		}
		int id = createObject(user);
		System.out.println(String.format("User #%d %s was successfully created", id, name));
		return user;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Set<User> getAllUsers() {
		Set<User> users = null;
		Session session = getSession();

		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<User> query = builder.createQuery(User.class);
		Root<User> root = query.from(User.class);

		users = session.createQuery(query.select(root)).getResultStream().collect(Collectors.toSet());

		return users;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteUser(int userId) {
		deleteObjectByID(userId);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteUser(User user) {
		deleteObject(user);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public User getUser(int id, boolean eager) throws HibernateException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		return getObject(id, eager);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public User getUserByName(String name) {
		Session session = getSession();

		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<User> query = builder.createQuery(User.class);
		Root<User> root = query.from(User.class);

		query.select(root).where(builder.like(root.get("name"), name));
		User user = session.createQuery(query).getResultStream().findAny().orElse(null);

		return user;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void updateUser(User user) {
		updateObject(user);
	}
}