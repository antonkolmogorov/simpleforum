package forum.entity;

import javax.persistence.Entity;

@Entity(name = "post")
public class Message extends ForumEntity {

	public Message() {
	}

	public Message(String text) {
		setText(text);
	}

	@Override
	public String toString() {
		return String.format("Message '%s', created at %t, modified at %t", text, created, modified);
	}
}