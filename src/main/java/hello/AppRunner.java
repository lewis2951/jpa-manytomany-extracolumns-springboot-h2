package hello;

import java.util.Arrays;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import hello.domain.Author;
import hello.domain.Book;
import hello.domain.BookAuthor;
import hello.repository.AuthorRepository;
import hello.repository.BookRepository;

@Component
public class AppRunner implements CommandLineRunner {

	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private AuthorRepository authorRepository;

	@Override
	@Transactional
	public void run(String... args) throws Exception {
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

}
