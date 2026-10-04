package jammy.dddshopmall.order.domain;

import jammy.dddshopmall.catalog.domain.ProductId;
import jammy.dddshopmall.common.model.Money;

// VO(Value Object)
public record OrderLine(
        ProductId productId,
        Money price,
        int quantity
) {

    public OrderLine {
        if (productId == null || price == null || quantity < 1 ) {
            throw new IllegalArgumentException("제품 아이디, 가격, 수량은 필수입니다.");
        }
    }

    public static OrderLine of(ProductId productId, Money price, int quantity) {
        return new OrderLine(productId, price, quantity);
    }

    public Money amounts() { return price.multiply(quantity); }
}