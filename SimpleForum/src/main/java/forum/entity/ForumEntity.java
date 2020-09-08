package forum.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity(name = "Entity")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ForumEntity {

	@JsonManagedReference
	@ManyToOne
	protected User author;

	@Column(name = "created")
	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	protected Date created;

	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	protected int id;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified")
	protected Date modified;

	@JsonManagedReference
	@ManyToOne
	protected Topic root;

	@Size(min = 5, message = "entity.length")
	@Column(name = "text")
	protected String text;

	public User getAuthor() {
		return author;
	}

	public Date getCreated() {
		return created;
	}

	public int getId() {
		return id;
	}

	public Date getModified() {
		return modified;
	}

	public Topic getRoot() {
		return root;
	}

	public String getText() {
		return text;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public void setRoot(Topic root) {
		this.root = root;
	}

	public void setText(String text) {
		this.text = text;
	}

}