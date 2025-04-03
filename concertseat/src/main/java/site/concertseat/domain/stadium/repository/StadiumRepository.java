package site.concertseat.domain.stadium.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.concertseat.domain.stadium.dto.SeatingWithCountDto;
import site.concertseat.domain.stadium.entity.Floor;
import site.concertseat.domain.stadium.entity.Seating;
import site.concertseat.domain.stadium.entity.Section;
import site.concertseat.domain.stadium.entity.Stadium;

import java.util.List;

@Repository
public interface StadiumRepository extends JpaRepository<Stadium, Integer> {
    @Query("select f " +
            "from Floor f " +
            "where f.stadium.id = :stadiumId")
    List<Floor> findFloorsByStadiumId(@Param("stadiumId") Integer stadiumId);

    @Query("select s " +
            "from Section s " +
            "where s.floor in :floors")
    List<Section> findSectionsByFloors(@Param("floors") List<Floor> floors);

    @Query("select s " +
            "from Seating s " +
            "where s.section in :sections")
    List<Seating> findSeatingBySections(@Param("sections") List<Section> sections);

    @Query("select new site.concertseat.domain.stadium.dto.SeatingWithCountDto(" +
            "s.id, s.name, count(r)) " +
            "from Seating s " +
            "left join Review r on r.seating = s " +
            "where s.section.id = :sectionId " +
            "group by s.id")
    List<SeatingWithCountDto> findSeatingWithCount(@Param("sectionId") Integer sectionId);
}
