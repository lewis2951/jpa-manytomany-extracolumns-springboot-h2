package hello.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hello.domain.Publisher;

public interface PublisherRepository extends JpaRepository<Publisher, Integer> {

}
