package forum.restful;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import forum.dao.UserDao;
import forum.entity.User;

@RestController
public class RegisterController {
	
	public static final String HANDSHAKE = "/handshake";
	public static final String REGISTER = "/register";

	@Autowired
	private UserDao dao;

	@RequestMapping(REGISTER)
	public User createUser(@RequestParam(name = "name", required = true) String name,
			@RequestParam(name = "email", required = true) String email,
			@RequestParam(name = "pwd", required = true) String password) {
		return dao.createUser(name, email, password);
	}
	
	@RequestMapping(HANDSHAKE)
	public void handshake() { }
}