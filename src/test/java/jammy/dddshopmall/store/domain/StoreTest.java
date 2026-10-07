package jammy.dddshopmall.store.domain;

import jammy.dddshopmall.catalog.domain.category.CategoryId;
import jammy.dddshopmall.catalog.domain.product.Product;
import jammy.dddshopmall.catalog.domain.product.ProductId;
import jammy.dddshopmall.common.model.Money;
import jammy.dddshopmall.member.domain.MemberId;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StoreTest {

    @Test
    void 생성() {

        Store store = createStore();

        assertThat(store.getState()).isEqualTo(StoreState.RUNNING);
    }

    @Test
    void 상품_등록() {

        Store store = createStore();
        Product product = store.createProduct(
                new ProductId(UUID.randomUUID().toString()),
                "상품1",
                new Money(10000),
                Set.of(
                        new CategoryId(UUID.randomUUID().toString()),
                        new CategoryId(UUID.randomUUID().toString())
                )
        );

        assertThat(product.getStoreId()).isEqualTo(store.getId());
    }

    @Test
    void 상점_정지_재개() {

        Store store = createStore();

        store.suspend();
        assertThat(store.getState()).isEqualTo(StoreState.SUSPENDED);

        store.resume();
        assertThat(store.getState()).isEqualTo(StoreState.RUNNING);
    }

    @Test
    void 정지된_상점은_상품을_등록하지_못한다() {

        Store store = createStore();
        store.suspend();

        assertThatThrownBy(() -> store.createProduct(
                new ProductId(UUID.randomUUID().toString()),
                "상품1",
                new Money(10000),
                Set.of(
                        new CategoryId(UUID.randomUUID().toString()),
                        new CategoryId(UUID.randomUUID().toString())
                ))
        ).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 상점_정지_중복해서_할_수_없다() {

        Store store = createStore();
        store.suspend();

        assertThatThrownBy(store::suspend)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 운영중인_상점에_재개할_수_없다() {

        Store store = createStore();

        assertThatThrownBy(store::resume)
                .isInstanceOf(IllegalStateException.class);
    }

    private Store createStore() {
        return Store.create(
                new StoreId(UUID.randomUUID().toString()),
                "홍길동",
                new MemberId(UUID.randomUUID().toString())
        );
    }
}