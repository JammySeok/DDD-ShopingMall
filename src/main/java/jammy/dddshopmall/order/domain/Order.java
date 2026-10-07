package jammy.dddshopmall.order.domain;

import jammy.dddshopmall.common.model.Money;

import java.util.List;

// Aggregate Root (Entity)
public class Order {

    // 주문 정보
    private OrderNo number;
    private Orderer orderer;
    private List<OrderLine> orderLines;
    private Money totalAmount;
    private ShippingInfo shippingInfo;
    private OrderState state;

    private Order(
            OrderNo number,
            Orderer orderer,
            List<OrderLine> orderLines,
            ShippingInfo shippingInfo
    ) {

        if (number == null || orderer == null || shippingInfo == null) throw new IllegalArgumentException("주문번호, 주문자, 배송정보는 필수입니다.");
        if (orderLines == null || orderLines.isEmpty()) throw new IllegalArgumentException("상품정보는 필수입니다.");

        this.number = number;
        this.orderer = orderer;
        this.orderLines = List.copyOf(orderLines);
        this.totalAmount = calculateTotalAmount(this.orderLines);
        this.shippingInfo = shippingInfo;
        this.state = OrderState.PENDING;
    }

    // 주문 생성
    public static Order create(
            OrderNo number,
            Orderer orderer,
            List<OrderLine> orderLines,
            ShippingInfo shippingInfo
    ) {
        return new Order(number, orderer, orderLines, shippingInfo);
    }


    /**
     * Getter
     */
    public OrderNo getNumber() { return number; }
    public Orderer getOrderer() { return orderer; }
    public List<OrderLine> getOrderLines() { return orderLines; }
    public Money getTotalAmount() { return totalAmount; }
    public ShippingInfo getShippingInfo() { return shippingInfo; }
    public OrderState getState() { return state; }


    /**
     * 도메인 로직
     */
    public void completePayment() {

        if (state != OrderState.PENDING) throw new IllegalStateException("결제 대기 상태가 아닙니다.");
        this.state = OrderState.PREPARING;
    }

    public void ship() {

        if (!state.isPreparing()) throw new IllegalStateException("상품 준비중이 아닙니다.");
        this.state = OrderState.SHIPPED;
    }

    public void cancel() {

        verifyNotCanceled();
        verifyNotYetShipped();
        this.state = OrderState.CANCELED;
    }

    public void changeShippingInfo(ShippingInfo newShippingInfo) {

        if (newShippingInfo == null) throw new IllegalArgumentException("배송정보는 필수입니다.");
        verifyNotCanceled();
        verifyNotYetShipped();
        this.shippingInfo = newShippingInfo;
    }

    private Money calculateTotalAmount(List<OrderLine> lines) {

        Money total = Money.of(0);
        for (OrderLine line : lines) {
            total = total.add(line.amounts());
        }
        return total;
    }

    private void verifyNotYetShipped() {
        if (!state.isBeforeShipping()) throw new IllegalStateException("이미 출고된 주문입니다.");
    }

    private void verifyNotCanceled() {
        if (state.isCanceled()) throw new IllegalStateException("이미 취소된 주문입니다.");
    }
}