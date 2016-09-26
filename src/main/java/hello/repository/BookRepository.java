package hello.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hello.domain.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {

}
