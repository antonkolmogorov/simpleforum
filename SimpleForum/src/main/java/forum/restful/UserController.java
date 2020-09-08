package forum.restful;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import forum.dao.UserDao;
import forum.entity.User;

@RestController
@RequestMapping(UserController.MAPPING)
public class UserController {
	
	public static final String MAPPING = "/user";
	public static final String LIST = "/list";

	@Autowired
	private UserDao dao;

	@RequestMapping(LIST)
	public Set<User> createUser() {
		return dao.getAllUsers();
	}

}