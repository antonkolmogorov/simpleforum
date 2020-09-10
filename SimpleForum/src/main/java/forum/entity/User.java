package forum.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import forum.entity.validators.Password;
import forum.entity.validators.Username;

@Entity(name = "user")
public class User {

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created")
	private Date created;

	@NotNull(message = "user.email.not.null")
	@Email(message = "user.email.correct")
	@Column(name = "email")
	private String email;

	@JsonBackReference
	@OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	private Set<ForumEntity> entities = new HashSet<ForumEntity>();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "name", unique = true)
	@Username(message = "user.name.exists")
	@NotEmpty(message = "user.name.not.empty")
	@Pattern(regexp = "[a-zA-Zà-ÿÀ-ß_0-9]+", message = "user.name.regex")
	private String name;
	
	@JsonIgnore
	@Password(message = "user.incorrect.password")
	@Column(name = "password")
	private String password;
	
	@JsonIgnore
	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	private UserRole role = UserRole.USER;

	public User() {
	}

	public User(String name, String email, String password) {
		setName(name);
		setEmail(email);
		setPassword(password);
	}

	public Date getCreated() {
		return created;
	}

	public String getEmail() {
		return email;
	}

	public Set<ForumEntity> getEntities() {
		return entities;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public UserRole getRole() {
		return role;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setEntities(Set<ForumEntity> entities) {
		this.entities = entities;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "User " + name + ", joined at " + created;
	}
}