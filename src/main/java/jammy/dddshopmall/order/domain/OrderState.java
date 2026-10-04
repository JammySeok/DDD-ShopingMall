package jammy.dddshopmall.order.domain;

public enum OrderState {

    PENDING,  // 결제 대기
    PREPARING,  // 상품 준비
    SHIPPED,  // 출고
    DELIVERING,  // 배송 중
    DELIVERED,  // 배송 완료
    CANCELED;  // 취소

    public boolean isBeforeShipping() {
        return this == PENDING || this == PREPARING;
    }
    public boolean isCanceled() {
        return this == CANCELED;
    }
}
