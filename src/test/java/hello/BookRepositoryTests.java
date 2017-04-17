package hello;

import hello.domain.Author;
import hello.domain.Book;
import hello.domain.BookAuthor;
import hello.repository.AuthorRepository;
import hello.repository.BookRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BookRepositoryTests {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @Before
    public void init() {
        Book spring = new Book("Spring in Action");
        Book springboot = new Book("Spring Boot in Action");

        Author lewis = new Author("Lewis");
        Author mark = new Author("Mark");
        Author peter = new Author("Peter");

        BookAuthor springLewis = new BookAuthor(spring, lewis, new Date());
        BookAuthor springMark = new BookAuthor(spring, mark, new Date());

        BookAuthor springbootLewis = new BookAuthor(springboot, lewis, new Date());
        BookAuthor springbootPeter = new BookAuthor(springboot, peter, new Date());

        spring.getBookAuthors().addAll(Arrays.asList(springLewis, springMark));
        springboot.getBookAuthors().addAll(Arrays.asList(springbootLewis, springbootPeter));

        authorRepository.save(Arrays.asList(lewis, mark, peter));
        bookRepository.save(Arrays.asList(spring, springboot));
    }

    @After
    public void deleteAll() {
        // 删除所有书籍和关联关系，但不删除作者
        bookRepository.deleteAll();

        // 删除所有作者，如果书籍和作者存在关联，则删除报错
        authorRepository.deleteAll();
    }

    @Test
    public void findAll() {
        assertThat(bookRepository.findAll()).hasSize(2);

        assertThat(authorRepository.findAll()).hasSize(3);
    }

    @Test
    public void findByName() {
        assertThat(bookRepository.findByName("Spring in Action")).isNotNull();

        assertThat(authorRepository.findByName("Lewis")).isNotNull();
    }

    @Test
    public void findByNameContaining() {
        assertThat(bookRepository.findByNameContaining("Spring")).hasSize(2);

        assertThat(authorRepository.findByNameContaining("e")).hasSize(2);
    }

    @Test
    public void margeBook() {
        Book book = bookRepository.findByName("Spring in Action");
        assertThat(book).isNotNull();

        book.setName("Spring in Action (4th Edition)");

        assertThat(bookRepository.findByName("Spring in Action")).isNull();
        assertThat(bookRepository.findByName("Spring in Action (4th Edition)")).isNotNull();
    }

    @Test
    public void deleteBook() {
        Book book = bookRepository.findByName("Spring Boot in Action");
        assertThat(book).isNotNull();

        bookRepository.delete(book);

        assertThat(bookRepository.findAll()).hasSize(1);
        assertThat(bookRepository.findByName("Spring Boot in Action")).isNull();

        assertThat(authorRepository.findAll()).hasSize(3);
        assertThat(authorRepository.findByName("Peter")).isNotNull();
    }

    @Test
    public void clearAuthor() {
        Book book = bookRepository.findByName("Spring in Action");
        assertThat(book).isNotNull();

        book.getBookAuthors().clear();

        assertThat(bookRepository.findAll()).hasSize(2);
        assertThat(bookRepository.findByName("Spring in Action").getBookAuthors()).isEmpty();

        assertThat(authorRepository.findAll()).hasSize(3);
    }

    @Test
    public void removeAuthor() {
        Book book = bookRepository.findByName("Spring Boot in Action");
        assertThat(book).isNotNull();

        Author author = authorRepository.findByName("Peter");
        assertThat(author).isNotNull();

        book.getBookAuthors().removeIf(bookAuthor -> bookAuthor.getAuthor().equals(author));

        assertThat(bookRepository.findAll()).hasSize(2);
        assertThat(bookRepository.findByName("Spring Boot in Action").getBookAuthors()).hasSize(1);

        assertThat(authorRepository.findAll()).hasSize(3);
        assertThat(authorRepository.findByName("Peter")).isNotNull();
    }

    @Test
    public void removeAllautors() {
        Book book = bookRepository.findByName("Spring in Action");
        assertThat(book).isNotNull();

        book.getBookAuthors().removeAll(book.getBookAuthors());

        assertThat(bookRepository.findAll()).hasSize(2);
        assertThat(bookRepository.findByName("Spring in Action").getBookAuthors()).isEmpty();

        assertThat(authorRepository.findAll()).hasSize(3);
    }

    @Test
    public void plusAuthor() {
        Book book = bookRepository.findByName("Spring in Action");
        assertThat(book).isNotNull();

        Author author = authorRepository.findByName("Jacob");
        assertThat(author).isNull();

        author = new Author("Jacob");
        BookAuthor bookAuthor = new BookAuthor(book, author, new Date());
        book.getBookAuthors().add(bookAuthor);
        authorRepository.save(author);

        assertThat(bookRepository.findByName("Spring in Action").getBookAuthors()).hasSize(3);

        assertThat(authorRepository.findAll()).hasSize(4);
        assertThat(authorRepository.findByName("Jacob")).isNotNull();
    }

    @Test
    public void deleteAuthor() {
        Book book = bookRepository.findByName("Spring Boot in Action");
        assertThat(book).isNotNull();
        bookRepository.delete(book);

        Author author = authorRepository.findByName("Peter");
        assertThat(author).isNotNull();
        authorRepository.delete(author);
        assertThat(authorRepository.findAll()).hasSize(2);
    }

    @Test
    public void deleteAllAuthors() {
        bookRepository.deleteAll();
        assertThat(bookRepository.findAll()).isEmpty();
        assertThat(authorRepository.findAll()).hasSize(3);

        authorRepository.deleteAll();
        assertThat(authorRepository.findAll()).isEmpty();
    }

}
