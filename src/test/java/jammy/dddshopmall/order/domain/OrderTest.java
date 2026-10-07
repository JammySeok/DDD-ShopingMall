package jammy.dddshopmall.order.domain;

import jammy.dddshopmall.catalog.domain.product.ProductId;
import jammy.dddshopmall.common.model.Address;
import jammy.dddshopmall.common.model.Money;
import jammy.dddshopmall.member.domain.MemberId;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    /**
     * Entity 테스트
     */
    @Test
    void 주문을_생성하면_총액이_계산되고_결제대기_상태가_된다() {

        Order order = createOrder();

        assertThat(order.getTotalAmount()).isEqualTo(Money.of(80000));
        assertThat(order.getState()).isEqualTo(OrderState.PENDING);
    }

    @Test
    void 결제() {

        Order order = createOrder();
        order.completePayment();

        assertThat(order.getState()).isEqualTo(OrderState.PREPARING);
    }

    @Test
    void 배송지_변경() {

        Order order = createOrder();
        order.completePayment();

        order.changeShippingInfo(
                new ShippingInfo(
                        new Receiver("홍길동", "010-1111-2222"),
                        new Address("00000", "마바사", "아자차카")
                )
        );

        assertThat(order.getShippingInfo().address().zipCode()).isEqualTo("00000");
        assertThat(order.getShippingInfo().address().address1()).isEqualTo("마바사");
        assertThat(order.getShippingInfo().address().address2()).isEqualTo("아자차카");
    }

    @Test
    void 출고_전에는_주문을_취소할_수_있다() {

        Order order = createOrder();
        order.completePayment();
        order.cancel();

        assertThat(order.getState()).isEqualTo(OrderState.CANCELED);
    }

    @Test
    void 출고() {

        Order order = createOrder();
        order.completePayment();
        order.ship();

        assertThat(order.getState()).isEqualTo(OrderState.SHIPPED);
    }

    @Test
    void 비어있는_주문항목() {
        assertThatThrownBy(() -> Order.create(
                OrderNo.of(UUID.randomUUID().toString()),
                new Orderer(
                        new MemberId(UUID.randomUUID().toString()),
                        "홍길동"
                ),
                List.of(),  // 빈 값
                new ShippingInfo(
                        new Receiver("홍길동", "010-1111-2222"),
                        new Address("12312", "가나다라", "")
                ))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 비어있는_배송항목() {
        assertThatThrownBy(() -> Order.create(
                OrderNo.of(UUID.randomUUID().toString()),
                new Orderer(
                        new MemberId(UUID.randomUUID().toString()),
                        "홍길동"
                ),
                List.of(
                        new OrderLine(
                                new ProductId(UUID.randomUUID().toString()),
                                new Money(10000),
                                4
                        ),
                        new OrderLine(
                                new ProductId(UUID.randomUUID().toString()),
                                new Money(20000),
                                2
                        )
                ),
                null  // 빈 값
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 출고된_주문은_배송지를_변경할_수_없다() {

        Order order = createOrder();
        order.completePayment();
        order.ship();

        // 배소지 변경
        assertThatThrownBy(() -> order.changeShippingInfo(
                new ShippingInfo(
                        new Receiver("홍길동", "010-1111-2222"),
                        new Address("12312", "가나다라", "")
                )
        )).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 출고된_상품은_주문취소를_할_수_없다() {

        Order order = createOrder();
        order.completePayment();
        order.ship();

        // 주문취소
        assertThatThrownBy(order::cancel)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void PREPARING_상태가_아니면_출고할_수_없다() {

        Order order = createOrder();

        // 출고
        assertThatThrownBy(order::ship)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void PENDING_상태가_아니면_결제할_수_없다() {

        Order order = createOrder();
        order.completePayment();

        // 다시한번 결제
        assertThatThrownBy(order::completePayment)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 주문취소_중복() {

        Order order = createOrder();
        order.completePayment();
        order.cancel();

        // 다시한번 주문 취소
        assertThatThrownBy(order::cancel)
                .isInstanceOf(IllegalStateException.class);
    }


    /**
     * VO 테스트
     */
    @Test
    void OrderLine_금액계산() {

        OrderLine orderLine = new OrderLine(
                new ProductId(UUID.randomUUID().toString()),
                new Money(10000),
                3
        );

        assertThat(orderLine.amounts()).isEqualTo(new Money(30000));
    }

    @Test
    void OrderLine_수량_0이하() {
        assertThatThrownBy(() -> new OrderLine(
                new ProductId(UUID.randomUUID().toString()),
                new Money(10000),
                -1
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void Money_음수금액() {
        assertThatThrownBy(() -> new Money(
                -10000
        )).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void Receiver_비어있는값() {
        assertThatThrownBy(() -> new Receiver(null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void Address_비어있는값() {
        assertThatThrownBy(() -> new Address(null, null, null))
                .isInstanceOf(IllegalArgumentException.class);
    }


    /**
     * 핼퍼 메서드
     */
    private Order createOrder() {
        return Order.create(
                OrderNo.of(UUID.randomUUID().toString()),
                new Orderer(
                        new MemberId(UUID.randomUUID().toString()),
                        "홍길동"
                ),
                List.of(
                        new OrderLine(
                                new ProductId(UUID.randomUUID().toString()),
                                new Money(10000),
                                4
                        ),
                        new OrderLine(
                                new ProductId(UUID.randomUUID().toString()),
                                new Money(20000),
                                2
                        )
                ),
                new ShippingInfo(
                        new Receiver("홍길동", "010-1111-2222"),
                        new Address("12312", "가나다라", "")
                )
        );
    }
}