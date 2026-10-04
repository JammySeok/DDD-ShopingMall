package jammy.dddshopmall.order.domain;

// VO(Value Object)
public record Receiver(
        String name,
        String phone
) {

    public Receiver {
        if (name == null || name.isBlank() || phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("이름과 전화번호는 필수입니다.");
        }
    }

    public static Receiver of(String name, String phone) {
        return new Receiver(name, phone);
    }
}