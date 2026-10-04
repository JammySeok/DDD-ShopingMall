package jammy.dddshopmall.common.model;

// VO(Value Object)
public record Address(
        String zipCode,
        String address1,
        String address2
) {

    public Address {
        if (zipCode == null || zipCode.isBlank() || address1 == null || address1.isBlank()) {
            throw new IllegalArgumentException("우편번호와 기본주소는 필수입니다.");
        }
    }

    public static Address of(String zipCode, String address1, String address2) {
        return new Address(zipCode, address1, address2);
    }
}
