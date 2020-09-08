package forum.entity.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import forum.dao.UserDao;

@Service
public class UsernameValidator implements ConstraintValidator<Username, String> {

	@Autowired(required = true)
	private UserDao userDao;

	@Override
	public boolean isValid(String name, ConstraintValidatorContext context) {
		return userDao.getUserByName(name) == null;
	}
}