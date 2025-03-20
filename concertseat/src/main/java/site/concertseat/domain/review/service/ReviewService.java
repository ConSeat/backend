package site.concertseat.domain.review.service;

import org.springframework.web.multipart.MultipartFile;
import site.concertseat.domain.member.entity.Member;
import site.concertseat.domain.review.dto.ImageUploadRes;
import site.concertseat.domain.review.dto.ReviewPostReq;
import site.concertseat.domain.review.dto.ReviewSearchRes;

import java.io.IOException;
import java.util.List;

public interface ReviewService {
    void postReview(Member member, Integer lineId, Integer concertId, ReviewPostReq reviewPostReq);

    ReviewSearchRes searchReview(Member member, Integer seatingId);

    ImageUploadRes uploadImage(Member member, List<MultipartFile> file) throws IOException;
}
