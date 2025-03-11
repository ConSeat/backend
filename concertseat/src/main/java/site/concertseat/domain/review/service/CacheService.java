package site.concertseat.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import site.concertseat.domain.review.repository.ReviewRepository;

@Service
@RequiredArgsConstructor
public class CacheService {
    private final ReviewRepository reviewRepository;

    @Cacheable(key = "#seatingId", value = "reviewCount:seating")
    public int getReviewCount(Integer seatingId) {
        return reviewRepository.countReviewsBySeatingId(seatingId);
    }
}
