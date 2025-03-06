package site.concertseat.domain.concert.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.concertseat.domain.concert.entity.Concert;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, Integer> {
}
