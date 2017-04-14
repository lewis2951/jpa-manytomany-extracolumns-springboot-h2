package hello.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.Set;

@Entity
public class Author implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "author")
    private Set<BookAuthor> bookAuthors;

    public Author() {
        super();
    }

    public Author(String name) {
        super();
        this.name = name;
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
        return String.format("Author [id=%s, name=%s]", id, name);
    }

}
