package forum.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity(name = "topic")
public class Topic extends ForumEntity {

	@JsonBackReference
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	private Set<ForumEntity> entities = new HashSet<ForumEntity>();

	public Topic() {
	}

	public Topic(String text) {
		setText(text);
	}

	public Set<ForumEntity> getEntities() {
		return entities;
	}

	public void setEntities(Set<ForumEntity> entities) {
		this.entities = entities;
	}

	@Override
	public String toString() {
		return String.format("Topic '%s', created at %t, modified at %t", text, created, modified);
	}
}