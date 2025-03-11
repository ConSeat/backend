package site.concertseat.domain.stadium.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.concertseat.domain.stadium.entity.Stadium;

@Repository
public interface StadiumRepository extends JpaRepository<Stadium, Integer> {
}
