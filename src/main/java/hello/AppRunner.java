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
        // --------------------------------------------------
        // 初始化
        // --------------------------------------------------
        init();
        display();

        // 精确查找
        findBookByName("in Action");
        findBookByName("Spring in Action");

        // 模糊查找，但不支持忽略大小写、忽略收尾空格
        findBookByNameContaining("action");
        findBookByNameContaining("Action ");
        findBookByNameContaining("Action");

        // --------------------------------------------------
        // 重新初始化，通过bookRepository演示对Book的操作
        // --------------------------------------------------
        reInit();
        display();

        // 更改书名
        margeBook("Spring in Action");
        display();

        // 删除书籍，作者不会被删除
        deleteBook("Spring Boot in Action");
        display();

        // --------------------------------------------------
        // 重新初始化，通过bookRepository演示对Author的操作
        // --------------------------------------------------
        reInit();
        display();

        // 追加作者
        // TODO: 2017/4/14 追加作者还需要进一步调试
//        plusAuthor("Spring in Action", "Jacob");
        plusAuthor("Spring in Action", "Mark");
        display();

        // 清空作者，只是清空关联关系，书籍和作者都还在
        clearAuthor("Spring in Action");
        display();

        // 移除作者，只是移除关联关系，书籍和作者都还在
        removeAuthor("Spring Boot in Action", "Peter");
        removeAuthor("Spring Boot in Action", "Lewis");
        display();

        reInit(); // 重新初始化
        display();

        // 移除全部作者，只是移除关联关系，书籍和作者都还在
        removeAllAuthors("Spring in Action");
        display();

        // --------------------------------------------------
        // 重新初始化，通过authorRepository演示对Author的操作
        // --------------------------------------------------
        reInit();
        display();

        // 删除作者，啥都删不掉，执行会报异常
//        deleteAuthor("Peter");
//        deleteAuthor("Lewis");
//
//        // 删除全部作者，啥都删不掉，执行会报异常
//        deleteAllAuthors();
    }

    /**
     * 显示
     */
    private void display() {
        logger.info("Display all books & authors ...");

        bookRepository.findAll().forEach(book -> {
            logger.info(book.toString());
        });
        authorRepository.findAll().forEach(author -> {
            logger.info(author.toString());
        });
    }

    /**
     * 初始化
     */
    private void init() {
        logger.info("Initial 2 books with 3 authors & 4 relationship ...");

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
     * 重新初始化
     */
    private void reInit() {
        deleteAllBooks();
        deleteAllAuthors();
        init();
    }

    /**
     * 删除全部（书籍和关联关系会被删除，作者不会被删除）
     */
    private void deleteAllBooks() {
        logger.info(String.format("Delete all books ..."));

        bookRepository.deleteAll();
    }

    /**
     * 精确查找
     *
     * @param name
     */
    private void findBookByName(String name) {
        logger.info(String.format("findBookByName [name:%s] ...", name));

        Book book = bookRepository.findByName(name);
        if (null == book) {
            logger.info(String.format("Book [%s]", "<empty>"));
        } else {
            logger.info(book.toString());
        }
    }

    /**
     * 模糊查找
     *
     * @param name
     */
    private void findBookByNameContaining(String name) {
        logger.info(String.format("findBookByNameContaining [name:%s] ...", name));

        bookRepository.findByNameContaining(name).forEach(book -> {
            logger.info(book.toString());
        });
    }

    /**
     * 修改
     *
     * @param name
     */
    private void margeBook(String name) {
        logger.info(String.format("margeBook [name:%s] ...", name));

        Book book = bookRepository.findByName(name);
        if (null == book) {
            return;
        }

        book.setName(name + " (4th Edition)");
        bookRepository.save(book);
    }

    /**
     * 删除（书籍和关联关系被删除，作者不会被删除）
     *
     * @param name
     */
    private void deleteBook(String name) {
        logger.info(String.format("deleteBook [name:$s]", name));

        Book book = bookRepository.findByName(name);
        if (null == book) {
            return;
        }

        bookRepository.delete(book);
    }

    /**
     * 为某书追加作者
     *
     * @param bookName
     * @param authorName
     */
    private void plusAuthor(String bookName, String authorName) {
        // TODO: 2017/4/14 追加作者还需要进一步调试
        logger.info(String.format("plusAuthor [book_name:%s, author_name:%s] ...", bookName, authorName));

        Book book = bookRepository.findByName(bookName);
        if (null == book) {
            return;
        }

        Author author = authorRepository.findByName(authorName);
        if (null != author) {
            return;
        }

        author = new Author(authorName);
        BookAuthor bookAuthor = new BookAuthor(book, author, new Date());
        book.getBookAuthors().add(bookAuthor);
        bookRepository.save(book);
    }

    /**
     * 清空某书的作者
     *
     * @param bookName
     */
    private void clearAuthor(String bookName) {
        logger.info(String.format("clearAuthor [book_name:%s]", bookName));

        Book book = bookRepository.findByName(bookName);
        if (null == book) {
            return;
        }

        book.getBookAuthors().clear();
        bookRepository.save(book);
    }

    /**
     * 移除某书的作者
     *
     * @param bookName
     * @param authorName
     */
    private void removeAuthor(String bookName, String authorName) {
        logger.info(String.format("removeAuthor [book_name:%s, author_name:%s] ...", bookName, authorName));

        Book book = bookRepository.findByName(bookName);
        if (null == book) {
            return;
        }

        Author author = authorRepository.findByName(authorName);
        if (null == author) {
            return;
        }

        book.getBookAuthors().removeIf(bookAuthor -> bookAuthor.getAuthor().equals(author));

        bookRepository.save(book);
    }

    /**
     * 移除某书的全部作者
     *
     * @param bookName
     */
    private void removeAllAuthors(String bookName) {
        logger.info(String.format("removeAllAuthors [book_name:%s]", bookName));

        Book book = bookRepository.findByName(bookName);
        if (null == book) {
            return;
        }

        book.getBookAuthors().removeAll(book.getBookAuthors());
        bookRepository.save(book);
    }

    /**
     * 删除
     *
     * @param name
     */
    private void deleteAuthor(String name) {
        logger.info(String.format("deleteAuthor [name:%s] ...", name));

        Author author = authorRepository.findByName(name);
        authorRepository.delete(author);
        authorRepository.flush();
    }

    /**
     * 删除全部，只删除作者，如果有作者和书籍有关联关系，则删除会报错
     */
    private void deleteAllAuthors() {
        logger.info(String.format("Delete all authors ..."));

        authorRepository.deleteAll();
    }

}


//        clearBookAuthor(); // 删除4个关联关系
//        display(); // 剩2本书和3位作者
//
//        // 清空数据继续后面的操作
//        reInit(); // 重新初始化
//        display();
//
//        deleteBook("Spring in Action"); // 删除2个关联关系，1本书
//        display();
//
//        deleteAllBooks(); // 删除2个关联关系，1本书
//        display(); // 剩3位作者


// =================================================


//
//
//
//
//    /**
//     * 清空书籍和作者的关联关系
//     */
//    private void clearBookAuthor() {
//        logger.info("Clear All [Book & Author]'s Relationship ...");
//
//        bookRepository.findAll().forEach(book -> {
//            book.getBookAuthors().clear();
//        });
//    }
//
//    /**
//     * 删除制定书籍
//     *
//     * @param bookName
//     */
//    private void deleteBook(String bookName) {
//        logger.info(String.format("Remove Book[name: %s] ...", bookName));
//
//        Book book = bookRepository.findByName(bookName);
//        bookRepository.delete(book);
//    }
//
//    private void deleteAllBooks() {
//        logger.info("Remove All Books ...");
//
//        bookRepository.deleteAll();
//    }
//
//    /**
//     * 删除制定作者
//     *
//     * @param authorName
//     */
//    private void deleteAuthor(String authorName) {
//        logger.info(String.format("Remove Author[name: %s] ...", authorName));
//
//        Author author = authorRepository.findByName(authorName);
//        authorRepository.delete(author);
//        authorRepository.flush();
//    }
