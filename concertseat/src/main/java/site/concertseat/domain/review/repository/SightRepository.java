package site.concertseat.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.concertseat.domain.review.entity.Sight;

@Repository
public interface SightRepository extends JpaRepository<Sight, Long> {
}
