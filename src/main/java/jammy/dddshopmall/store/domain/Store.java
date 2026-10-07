package jammy.dddshopmall.store.domain;

import jammy.dddshopmall.catalog.domain.category.CategoryId;
import jammy.dddshopmall.catalog.domain.product.Product;
import jammy.dddshopmall.catalog.domain.product.ProductId;
import jammy.dddshopmall.common.model.Money;
import jammy.dddshopmall.member.domain.MemberId;

import java.util.Set;

public class Store {

    private StoreId id;
    private String name;
    private StoreState state;
    private MemberId ownerId;

    private Store(
            StoreId id,
            String name,
            MemberId ownerId
    ) {

        if (id == null) throw new IllegalArgumentException("상점 아이디는 필수입니다.");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("상점 이름은 필수입니다.");
        if (ownerId == null) throw new IllegalArgumentException("상점 주인은 필수입니다.");

        this.id = id;
        this.name = name;
        this.state = StoreState.RUNNING;
        this.ownerId = ownerId;
    }

    public static Store create(
            StoreId id,
            String name,
            MemberId ownerId
    ) {
        return new Store(id, name, ownerId);
    }


    /**
     * Getter
     */
    public StoreId getId() { return id; }
    public String getName() { return name; }
    public StoreState getState() { return state; }
    public MemberId getOwnerId() { return ownerId; }

    /**
     * 도메인 로직
     */
    public Product createProduct(
            ProductId productId,
            String name,
            Money price,
            Set<CategoryId> categoryIds
    ) {
        if (state.isSuspended()) throw new IllegalStateException("영업 정지된 상점은 상품을 등록할 수 없습니다.");
        return Product.create(productId, this.id, name, price, categoryIds);
    }

    public void suspend() {
        if (state.isSuspended()) throw new IllegalStateException("이미 정지된 상점입니다.");
        this.state = StoreState.SUSPENDED;
    }

    public void resume()  {
        if (state.isRunning()) throw new IllegalStateException("이미 활성화된 상점입니다.");
        this.state = StoreState.RUNNING;
    }
}