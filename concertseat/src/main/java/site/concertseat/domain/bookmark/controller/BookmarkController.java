package site.concertseat.domain.bookmark.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.concertseat.domain.bookmark.dto.req.BookmarkReviewSearchReq;
import site.concertseat.domain.bookmark.dto.res.BookmarkReviewSearchRes;
import site.concertseat.domain.bookmark.dto.res.BookmarkStadiumListRes;
import site.concertseat.domain.bookmark.service.BookmarkService;
import site.concertseat.domain.member.entity.Member;
import site.concertseat.global.argument_resolver.LoginMember;
import site.concertseat.global.dto.ResponseDto;

import static site.concertseat.global.statuscode.SuccessCode.OK;

@RestController
@RequestMapping("/api/members/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @GetMapping("/stadiums")
    public ResponseDto<BookmarkStadiumListRes> bookmarkStadiumList(@LoginMember Member member) {
        BookmarkStadiumListRes res = bookmarkService.findStadium(member);

        return ResponseDto.success(OK, res);
    }

    @GetMapping
    public ResponseDto<BookmarkReviewSearchRes> bookmarkReviewSearch(@LoginMember Member member,
                                                                     @Valid @ModelAttribute BookmarkReviewSearchReq req,
                                                                     @PageableDefault(size = 9) Pageable pageable) {
        BookmarkReviewSearchRes res = bookmarkService.searchReview(member, req, pageable);

        return ResponseDto.success(OK, res);
    }
}
