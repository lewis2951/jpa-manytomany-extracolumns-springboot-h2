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

import javax.persistence.Transient;
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

    /**
     * 初始化：2本书，3位作者，4个关联关系
     */
    @Before
    @Transient
    public void save() {
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

    /**
     * 清空所有数据
     */
    @After
    public void deleteAll() {
        bookRepository.deleteAll();
    }

    @Test
    public void findAll() {
        assertThat(bookRepository.findAll()).hasSize(2);
        assertThat(authorRepository.findAll()).hasSize(3);
    }

    /**
     * 清空书籍和作者的关联关系
     */
    @Test
    public void clearBookAuthor() {
        bookRepository.findAll().forEach(book -> {
            book.getBookAuthors().clear();
        });

        assertThat(bookRepository.findAll()).hasSize(2);
        assertThat(authorRepository.findAll()).hasSize(3);
    }

    /**
     * 删除其中一本书
     */
    @Test
    public void deleteBook() {
        Book book = bookRepository.findByName("Spring in Action");
        bookRepository.delete(book);

        assertThat(bookRepository.findAll()).hasSize(1);
        assertThat(authorRepository.findAll()).hasSize(3);
    }

    /**
     * 删除所有书籍
     */
    @Test
    public void deleteAllBooks() {
        bookRepository.deleteAll();

        assertThat(bookRepository.findAll()).isEmpty();
        assertThat(authorRepository.findAll()).hasSize(3);
    }

}
