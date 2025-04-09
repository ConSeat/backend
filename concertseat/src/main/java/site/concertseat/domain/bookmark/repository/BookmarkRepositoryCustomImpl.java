package site.concertseat.domain.bookmark.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import site.concertseat.domain.bookmark.dto.BookmarkReviewDto;

import java.time.LocalDateTime;
import java.util.List;

import static site.concertseat.domain.bookmark.entity.QBookmark.bookmark;
import static site.concertseat.domain.review.entity.QReview.review;
import static site.concertseat.domain.stadium.entity.QFloor.floor;
import static site.concertseat.domain.stadium.entity.QSeating.seating;
import static site.concertseat.domain.stadium.entity.QSection.section;
import static site.concertseat.domain.stadium.entity.QStadium.stadium;

@RequiredArgsConstructor
public class BookmarkRepositoryCustomImpl implements BookmarkRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<BookmarkReviewDto> findBookmarkReviews(Long memberId, Integer stadiumId, LocalDateTime lastModifiedAt, Pageable pageable) {
        BooleanBuilder where = new BooleanBuilder();

        if (lastModifiedAt != null) {
            where.and(bookmark.modifiedAt.lt(lastModifiedAt));
        }

        List<BookmarkReviewDto> reviews = queryFactory.select(
                Projections.constructor(BookmarkReviewDto.class,
                        review.id,
                        review.thumbnail,
                        floor.name,
                        section.name,
                        seating.name,
                        bookmark.modifiedAt))
                .from(bookmark)
                .join(bookmark.review, review)
                .join(review.seating, seating)
                .join(seating.section, section)
                .join(section.floor, floor)
                .join(floor.stadium, stadium)
                .where(bookmark.member.id.eq(memberId))
                .where(stadium.id.eq(stadiumId))
                .where(where)
                .orderBy(bookmark.modifiedAt.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = reviews.size() > pageable.getPageSize();
        if (hasNext) reviews.remove(reviews.size() - 1);

        return new SliceImpl<>(reviews, pageable, hasNext);
    }
}
