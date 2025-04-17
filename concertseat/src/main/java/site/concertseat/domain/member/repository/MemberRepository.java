package site.concertseat.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import site.concertseat.domain.member.dto.MemberInfo;
import site.concertseat.domain.member.entity.Member;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUuid(String uuid);

    Optional<Member> findBySocialId(String socialId);

    @Query("select new site.concertseat.domain.member.dto.MemberInfo(" +
            "m.nickname, " +
            "m.src, " +
            "m.socialId) " +
            "from Member m " +
            "where m.id = :memberId")
    Optional<MemberInfo> findMemberInfoByMemberId(Long memberId);
}
