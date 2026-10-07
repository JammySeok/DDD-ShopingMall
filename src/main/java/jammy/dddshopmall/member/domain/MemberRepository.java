package jammy.dddshopmall.member.domain;

import java.util.Optional;

public interface MemberRepository {

    Optional<Member> findById(MemberId id);
    void save(Member member);
}
