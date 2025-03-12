package site.concertseat.domain.concert.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.concertseat.domain.concert.entity.Concert;

import java.util.List;

import static site.concertseat.domain.concert.entity.QConcert.concert;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements CustomConcertRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Concert> searchConcerts(Integer stadiumId, String[] queries) {
        JPAQuery<Concert> query = queryFactory
                .selectFrom(concert)
                .where(concert.stadium.id.eq(stadiumId))
                .orderBy(concert.name.asc());

        for (String word : queries) {
            query.where(concert.name.contains(word));
        }

        return query.fetch();
    }
}
