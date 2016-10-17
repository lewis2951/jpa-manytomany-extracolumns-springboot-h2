package hello.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hello.domain.Author;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

}
