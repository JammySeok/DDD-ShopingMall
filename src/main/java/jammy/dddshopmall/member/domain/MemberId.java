package jammy.dddshopmall.member.domain;

public record MemberId(
        String id
) {

    public MemberId {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("회원 아이디는 필수입니다.");
    }


}
