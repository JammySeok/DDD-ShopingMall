package jammy.dddshopmall.order.domain;

// VO(Value Object)
public record OrderNo(
        String number
) {

    public OrderNo {
        if (number == null || number.isBlank()) throw new IllegalArgumentException("주문번호는 필수입니다.");
    }

    public static OrderNo of(String number) {
        return new OrderNo(number);
    }
}