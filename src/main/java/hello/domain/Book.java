package hello.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Book implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Integer id;

	private String name;

	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
	private Set<BookAuthor> bookAuthors;

	public Book() {
		super();
	}

	public Book(String name) {
		super();
		this.name = name;
		bookAuthors = new HashSet<>();
	}

	public Book(String name, Set<BookAuthor> bookAuthors) {
		super();
		this.name = name;
		this.bookAuthors = bookAuthors;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<BookAuthor> getBookAuthors() {
		return bookAuthors;
	}

	public void setBookAuthors(Set<BookAuthor> bookAuthors) {
		this.bookAuthors = bookAuthors;
	}

	@Override
	public String toString() {
		// TODO bookAuthors --> authors
		return String.format("Book [id=%s, name=%s, bookAuthors=%s]", id, name, bookAuthors);
	}

}
