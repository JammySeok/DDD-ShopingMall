package jammy.dddshopmall.catalog.domain.product;

import jammy.dddshopmall.catalog.domain.category.CategoryId;
import jammy.dddshopmall.common.model.Money;
import jammy.dddshopmall.store.domain.StoreId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void 생성() {
        // StroeTest에서 확인
    }

    @Test
    void 카테고리가_없으면_상품을_생성할_수_없다() {
        Assertions.assertThatThrownBy(() -> Product.create(
                new ProductId(UUID.randomUUID().toString()),
                new StoreId(UUID.randomUUID().toString()),
                "상품1",
                new Money(20000),
                Set.of()
                )
        ).isInstanceOf(IllegalArgumentException.class);
    }

    private Product createProduct() {
        return Product.create(
                new ProductId(UUID.randomUUID().toString()),
                new StoreId(UUID.randomUUID().toString()),
                "상품1",
                new Money(20000),
                Set.of(
                        new CategoryId(UUID.randomUUID().toString()),
                        new CategoryId(UUID.randomUUID().toString())
                )
        );
    }
}