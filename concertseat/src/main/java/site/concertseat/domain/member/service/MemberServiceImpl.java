package site.concertseat.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.concertseat.domain.bookmark.repository.BookmarkRepository;
import site.concertseat.domain.member.dto.MemberInfo;
import site.concertseat.domain.member.dto.req.MemberModifyReq;
import site.concertseat.domain.member.dto.res.MemberProfileRes;
import site.concertseat.domain.member.dto.res.MemberSearchRes;
import site.concertseat.domain.member.dto.res.MemberModifyRes;
import site.concertseat.domain.member.entity.Member;
import site.concertseat.domain.member.repository.MemberRepository;
import site.concertseat.domain.review.repository.ReviewRepository;
import site.concertseat.global.exception.CustomException;
import site.concertseat.global.s3.S3Service;

import static site.concertseat.global.statuscode.ErrorCode.NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final BookmarkRepository bookmarkRepository;
    private final ReviewRepository reviewRepository;
    private final S3Service s3Service;

    @Override
    public MemberSearchRes searchMember(Member member) {
        MemberInfo memberInfo = memberRepository.findMemberInfoByMemberId(member.getId())
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        Long bookmarkCount = bookmarkRepository.countBookmarkByMemberId(member.getId());
        Long reviewCount = reviewRepository.countMemberReviews(member.getId());

        return new MemberSearchRes(memberInfo, bookmarkCount, reviewCount);
    }

    @Override
    @Transactional
    public MemberModifyRes modifyMember(Member member, MemberModifyReq memberModifyReq) {
        updateMember(member, memberModifyReq);

        return new MemberModifyRes(member.getNickname(), member.getSrc());
    }

    private void updateMember(Member member, MemberModifyReq memberModifyReq) {
        if(memberModifyReq.getNickname() != null) {
            member.updateNickname(memberModifyReq.getNickname());
        }

        if(memberModifyReq.getFile() != null) {
            s3Service.deleteFolder("members/"+member.getId()+"/profile");
            String newImageSrc = s3Service.upload(memberModifyReq.getFile(), "members/"+member.getId()+"/profile", 1);
            member.updateSrc(newImageSrc);
        }
    }

    @Override
    public MemberProfileRes getMemberInfo(Member member) {
        MemberInfo memberInfo = memberRepository.findMemberInfoByMemberId(member.getId())
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        return new MemberProfileRes(memberInfo);
    }
}
