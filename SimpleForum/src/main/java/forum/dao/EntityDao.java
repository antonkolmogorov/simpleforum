package forum.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class EntityDao<T> {

	private Class<T> clazz;
	private Set<Method> lazyMethods;
	@Autowired(required = true)
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public EntityDao() {
		clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		lazyMethods = Arrays.stream(clazz.getMethods())
				.filter(m -> Collection.class.isAssignableFrom(m.getReturnType())).collect(Collectors.toSet());
	}

	protected int createObject(T t) {
		return (int) getSession().save(t);
	}

	protected void deleteObject(T t) {
		getSession().delete(t);
	}

	protected void deleteObjectByID(int id) {
		getSession().delete(getSession().get(clazz, id));
	}

	protected T getObject(int id, boolean eager) throws HibernateException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		T t = getSession().get(clazz, id);
		if (eager && t != null) {
			for (Method method : lazyMethods) {
				Hibernate.initialize(method.invoke(t, (Object[]) null));
			}
		}
		return t;
	}

	protected Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (Exception e) {
			return sessionFactory.openSession();
		}
	}

	protected void updateObject(T t) {
		getSession().saveOrUpdate(t);
	}
}