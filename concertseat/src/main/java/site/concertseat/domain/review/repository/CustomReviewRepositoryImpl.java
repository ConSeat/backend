package site.concertseat.domain.review.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Repository;
import site.concertseat.domain.review.dto.MyReviewDto;
import site.concertseat.domain.review.dto.ReviewDto;
import site.concertseat.domain.review.dto.req.MyReviewSearchReq;
import site.concertseat.domain.review.dto.req.ReviewListReq;
import site.concertseat.domain.review.dto.ReviewWithLikesCount;
import site.concertseat.domain.review.entity.Review;
import site.concertseat.global.exception.CustomException;

import java.util.List;

import static site.concertseat.domain.member.entity.QMember.member;
import static site.concertseat.domain.review.entity.QLikes.likes;
import static site.concertseat.domain.review.entity.QReview.review;
import static site.concertseat.domain.review.entity.QReviewFeature.reviewFeature;
import static site.concertseat.domain.review.entity.QReviewObstruction.reviewObstruction;
import static site.concertseat.domain.review.enums.ReviewStatus.APPROVED;
import static site.concertseat.domain.stadium.entity.QFloor.floor;
import static site.concertseat.domain.stadium.entity.QSeating.seating;
import static site.concertseat.domain.stadium.entity.QSection.section;
import static site.concertseat.domain.stadium.entity.QStadium.stadium;
import static site.concertseat.global.statuscode.ErrorCode.NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class CustomReviewRepositoryImpl implements CustomReviewRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<ReviewDto> findReviews(ReviewListReq reviewListReq, Integer seatingId, Pageable pageable) {
        JPAQuery<Tuple> query = queryFactory
                .select(review, likes.count())
                .from(review)
                .where(review.seating.id.eq(seatingId)
                        .and(review.status.eq(APPROVED)))
                .join(review.member).fetchJoin()
                .leftJoin(likes)
                .on(likes.review.id.eq(review.id))
                .groupBy(review.id)
                .limit(pageable.getPageSize());

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        booleanBuilder.and(filter(query, reviewListReq));
        booleanBuilder.and(sort(query, reviewListReq, pageable));

        query.having(booleanBuilder);

        List<ReviewDto> reviews = query.fetch()
                .stream()
                .map(tuple -> new ReviewWithLikesCount(tuple.get(0, Review.class), tuple.get(1, Long.class)))
                .map(ReviewDto::toDto)
                .toList();

        return new SliceImpl<>(reviews, pageable, reviews.size() == pageable.getPageSize());
    }

    private BooleanBuilder sort(JPAQuery<Tuple> query, ReviewListReq reviewListReq, Pageable pageable) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        for (Order order : pageable.getSort()) {
            switch (order.getProperty()) {
                case "createdAt" -> orderByCreatedAt(query, reviewListReq);
                case "likesCount" -> booleanBuilder.and(orderByLikesCount(query, reviewListReq));
            }
        }

        return booleanBuilder;
    }

    private void orderByCreatedAt(JPAQuery<Tuple> query, ReviewListReq reviewListReq) {
        Review lastReview = null;

        if (reviewListReq.getLastReviewId() != null) {
            lastReview = queryFactory.selectFrom(review)
                    .where(review.id.eq(reviewListReq.getLastReviewId()))
                    .fetchOne();

            if (lastReview == null) {
                throw new CustomException(NOT_FOUND);
            }
        }

        if (lastReview != null) {
            query.where(review.createdAt
                    .lt(lastReview.getCreatedAt())
                    .or(review.createdAt
                            .eq(lastReview.getCreatedAt())
                            .and(review.id
                                    .lt(lastReview.getId()))));
        }

        query.orderBy(review.createdAt.desc())
                .orderBy(review.id.desc());
    }

    private BooleanBuilder orderByLikesCount(JPAQuery<Tuple> query, ReviewListReq reviewListReq) {
        Tuple tuple = null;

        if (reviewListReq.getLastReviewId() != null) {
            tuple = queryFactory.select(review, likes.count())
                    .from(review)
                    .leftJoin(likes)
                    .on(likes.review.id.eq(review.id))
                    .groupBy(review.id)
                    .where(review.id.eq(reviewListReq.getLastReviewId()))
                    .fetchOne();

            if (tuple == null) {
                throw new CustomException(NOT_FOUND);
            }
        }

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (tuple != null) {
            Review lastReview = tuple.get(0, Review.class);
            Long likesCount = tuple.get(1, Long.class);

            booleanBuilder.and(likes.count()
                    .lt(likesCount)
                    .or(likes.count()
                            .eq(likesCount)
                            .and(review.id
                                    .lt(lastReview.getId()))));
        }

        query.orderBy(likes.count().desc())
                .orderBy(review.id.desc());

        return booleanBuilder;
    }

    private BooleanBuilder filter(JPAQuery<Tuple> query, ReviewListReq reviewListReq) {
        List<Integer> features = reviewListReq.getFeatures();
        List<Integer> obstructions = reviewListReq.getObstructions();

        query.leftJoin(reviewFeature)
                .on(reviewFeature.review.id.eq(review.id)
                        .and(reviewFeature.feature.id.in(features)));

        query.leftJoin(reviewObstruction)
                .on(reviewObstruction.review.id.eq(review.id)
                        .and(reviewObstruction.obstruction.id.in(obstructions)));

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        booleanBuilder.and(reviewFeature.countDistinct().eq((long) features.size()));
        booleanBuilder.and(reviewObstruction.countDistinct().eq((long) obstructions.size()));

        return booleanBuilder;
    }

    @Override
    public Slice<MyReviewDto> findMyReviews(Long memberId, MyReviewSearchReq req, Pageable pageable) {
        BooleanBuilder where = new BooleanBuilder();

        if (req.getLastReviewId() != null) {
            where.and(review.id.lt(req.getLastReviewId()));
        }

        List<MyReviewDto> reviews = queryFactory.select(
                        Projections.constructor(MyReviewDto.class,
                                review.id,
                                review.thumbnail,
                                floor.name,
                                section.name,
                                seating.name,
                                review.status))
                .from(review)
                .join(review.member, member)
                .join(review.seating, seating)
                .join(seating.section, section)
                .join(section.floor, floor)
                .join(floor.stadium, stadium)
                .where(member.id.eq(memberId))
                .where(stadium.id.eq(req.getStadiumId()))
                .where(where)
                .orderBy(review.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = reviews.size() > pageable.getPageSize();
        if (hasNext) reviews.remove(reviews.size() - 1);

        return new SliceImpl<>(reviews, pageable, hasNext);
    }
}
