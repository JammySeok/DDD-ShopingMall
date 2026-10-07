package jammy.dddshopmall.member.domain;

import jammy.dddshopmall.common.model.Address;

public class Member {

    private MemberId id;
    private String name;
    private Address address;

    private Member(
            MemberId id,
            String name,
            Address address
    ) {

        if (id == null) throw new IllegalArgumentException("회원아이디는 필수입니다.");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("성함은 필수입니다.");

        this.id = id;
        this.name = name;
        this. address = address;
    }

    public static Member create(
            MemberId id,
            String name,
            Address address
    ) {
        return new Member(id, name, address);
    }


    /**
     * Getter
     */
    public MemberId getId() { return id; }
    public String getName() { return name; }
    public Address getAddress() { return address; }

    /**
     * 비지니스 로직
     */
    public void changeAddress(Address address) {

        if (address == null) throw new IllegalArgumentException("주소는 필수입니다.");
        this.address = address;
    }
}