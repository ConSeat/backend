package site.concertseat.domain.admin.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import site.concertseat.domain.admin.dto.AdminReviewDto;
import site.concertseat.domain.admin.dto.req.AdminReviewListReq;

import java.time.LocalDateTime;
import java.util.List;

import static site.concertseat.domain.member.entity.QMember.member;
import static site.concertseat.domain.review.entity.QReview.review;
import static site.concertseat.domain.stadium.entity.QFloor.floor;
import static site.concertseat.domain.stadium.entity.QSeating.seating;
import static site.concertseat.domain.stadium.entity.QSection.section;
import static site.concertseat.domain.stadium.entity.QStadium.stadium;

@Repository
@RequiredArgsConstructor
public class CustomAdminReviewRepositoryImpl implements CustomAdminReviewRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AdminReviewDto> findAdminReviews(Pageable pageable, AdminReviewListReq adminReviewListReq) {
        JPAQuery<AdminReviewDto> query = queryFactory
                .select(Projections.constructor(AdminReviewDto.class, review))
                .from(review)
                .join(review.member, member).fetchJoin()
                .join(review.seating, seating).fetchJoin()
                .join(seating.section, section).fetchJoin()
                .join(section.floor, floor).fetchJoin()
                .join(floor.stadium, stadium).fetchJoin()
                .orderBy(review.modifiedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        filter(query, adminReviewListReq);

        List<AdminReviewDto> content = query.fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(review.count())
                .from(review)
                .join(review.member, member)
                .join(review.seating, seating)
                .join(seating.section, section)
                .join(section.floor, floor)
                .join(floor.stadium, stadium);

        filter(countQuery, adminReviewListReq);

        Long total = countQuery.fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }

    private void filter(JPAQuery<?> query, AdminReviewListReq adminReviewListReq) {
        if (adminReviewListReq.getStartDate() != null) {
            LocalDateTime startOfDay = adminReviewListReq.getStartDate().atStartOfDay();
            query.where(review.createdAt.goe(startOfDay));
        }

        if (adminReviewListReq.getEndDate() != null) {
            LocalDateTime endOfDay = adminReviewListReq.getEndDate().plusDays(1).atStartOfDay();
            query.where(review.createdAt.lt(endOfDay));
        }

        if (adminReviewListReq.getStadiumId() != null) {
            query.where(stadium.id.eq(adminReviewListReq.getStadiumId()));
        }

        if (adminReviewListReq.getSectionId() != null) {
            query.where(section.id.eq(adminReviewListReq.getSectionId()));
        }

        if (adminReviewListReq.getStatus() != null) {
            query.where(review.status.stringValue().eq(adminReviewListReq.getStatus()));
        }

        if (adminReviewListReq.getQuery() != null) {
            query.where(member.nickname.contains(adminReviewListReq.getQuery()));
        }
    }
}
