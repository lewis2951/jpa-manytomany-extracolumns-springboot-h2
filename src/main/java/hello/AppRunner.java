package hello;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import hello.domain.Book;
import hello.domain.BookPublisher;
import hello.domain.Publisher;
import hello.repository.BookRepository;
import hello.repository.PublisherRepository;

@Component
public class AppRunner implements CommandLineRunner {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private PublisherRepository publisherRepository;

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		Book bookA = new Book("Book A");
		Publisher publisherA = new Publisher("Publisher A");

		BookPublisher bookPublisher = new BookPublisher();
		bookPublisher.setBook(bookA);
		bookPublisher.setPublisher(publisherA);
		bookPublisher.setPublishedDate(new Date());

		bookA.getBookPublishers().add(bookPublisher);

		publisherRepository.save(publisherA);
		bookRepository.save(bookA);
	}

}
