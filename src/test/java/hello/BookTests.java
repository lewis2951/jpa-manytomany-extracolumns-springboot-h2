package hello;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import hello.domain.Author;
import hello.domain.Book;
import hello.repository.AuthorRepository;
import hello.repository.BookRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BookTests {

	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private AuthorRepository authorRepository;

	@Before
	public void save() {
		Book spring = new Book("Spring in Action");
		Book springboot = new Book("Spring Boot in Action");
		Book springdata = new Book("Spring Data in Action");

		Author lewis = new Author("Lewis");
		Author mark = new Author("Mark");
		Author peter = new Author("Peter");

		authorRepository.save(Arrays.asList(lewis, mark, peter));
		bookRepository.save(Arrays.asList(spring, springboot, springdata));
	}

	@After
	public void deleteAll() {
		bookRepository.deleteAll();
	}

	@Test
	public void findAll() {
		assertThat(bookRepository.findAll()).hasSize(3);
		assertThat(authorRepository.findAll()).hasSize(3);
	}

}