package site.concertseat.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.concertseat.domain.bookmark.repository.BookmarkRepository;
import site.concertseat.domain.member.dto.MemberInfo;
import site.concertseat.domain.member.dto.res.MemberSearchRes;
import site.concertseat.domain.member.entity.Member;
import site.concertseat.domain.member.repository.MemberRepository;
import site.concertseat.domain.review.repository.ReviewRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public MemberSearchRes searchMember(Member member) {
        MemberInfo memberInfo = memberRepository.findMemberInfoByMemberId(member.getId());
        Long bookmarkCount = bookmarkRepository.countBookmarkByMemberId(member.getId());
        Long reviewCount = reviewRepository.countMemberReviews(member.getId());

        return new MemberSearchRes(memberInfo, bookmarkCount, reviewCount);
    }
}
