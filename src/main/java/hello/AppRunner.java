package hello;

import hello.domain.Author;
import hello.domain.Book;
import hello.domain.BookAuthor;
import hello.repository.AuthorRepository;
import hello.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Date;

@Component
public class AppRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AppRunner.class);

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        init(); // 插入2本书，3位作者，4个关联关系
        display();

        clearBookAuthor(); // 删除4个关联关系
        display(); // 剩2本书和3位作者

        // 清空数据继续后面的操作
        reInit(); // 重新初始化
        display();

        deleteBook("Spring in Action"); // 删除2个关联关系，1本书
        display();

        deleteAllBooks(); // 删除2个关联关系，1本书
        display(); // 剩3位作者
    }

    /**
     * 数据展示
     */
    private void display() {
        logger.info("Display Books & Authors ...");

        bookRepository.findAll().forEach(book -> {
            logger.info(book.toString());
        });
        authorRepository.findAll().forEach(author -> {
            logger.info(author.toString());
        });
    }

    /**
     * 数据初始化
     */
    private void init() {
        logger.info("Initial Books & Authors ...");

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
     * 清空书籍和作者的关联关系
     */
    private void clearBookAuthor() {
        logger.info("Clear All [Book & Author]'s Relationship ...");

        bookRepository.findAll().forEach(book -> {
            book.getBookAuthors().clear();
        });
    }

    /**
     * 重新初始化
     */
    private void reInit() {
        deleteAll();
        init();
    }

    /**
     * 清空
     */
    private void deleteAll() {
        logger.info("Delete All ...");

        authorRepository.deleteAll();
        bookRepository.deleteAll();
    }

    /**
     * 删除制定书籍
     *
     * @param bookName
     */
    private void deleteBook(String bookName) {
        logger.info(String.format("Remove Book[name: %s] ...", bookName));

        Book book = bookRepository.findByName("Spring Boot in Action");
        bookRepository.delete(book);
    }

    private void deleteAllBooks() {
        logger.info("Remove All Books ...");

        bookRepository.deleteAll();
    }

    /**
     * 删除制定作者
     *
     * @param authorName
     */
    private void deleteAuthor(String authorName) {
        logger.info(String.format("Remove Author[name: %s] ...", authorName));

        Author author = authorRepository.findByName(authorName);
        authorRepository.delete(author);
        authorRepository.flush();
    }

}
