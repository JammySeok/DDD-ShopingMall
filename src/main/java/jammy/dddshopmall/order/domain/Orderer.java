package jammy.dddshopmall.order.domain;

import jammy.dddshopmall.member.domain.MemberId;

public record Orderer (
        MemberId memberId,
        String name
) {

    public Orderer {
        if (memberId == null) throw new IllegalArgumentException("회원 아이디는 필수입니다.");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("주문자 이름은 필수입니다.");
    }

    public static Orderer of(
            MemberId memberId,
            String name
    ) {
        return new Orderer(memberId, name);
    }
}