package jammy.dddshopmall.catalog.domain.product;

import jammy.dddshopmall.catalog.domain.category.CategoryId;
import jammy.dddshopmall.common.model.Money;
import jammy.dddshopmall.store.domain.StoreId;

import java.util.Set;

public class Product {

    private ProductId id;
    private StoreId storeId;
    private String name;
    private Money price;
    private Set<CategoryId> categoryIds;

    private Product(
            ProductId id,
            StoreId storeId,
            String name,
            Money price,
            Set<CategoryId> categoryIds
    ) {

        if (id == null) throw new IllegalArgumentException("제품 아이디는 필수입니다.");
        if (storeId == null) throw new IllegalArgumentException("상점은 필수입니다.");
        if (name ==  null || name.isBlank()) throw new IllegalArgumentException("이름은 필수입니다.");
        if (price == null) throw new IllegalArgumentException("가격은 필수입니다.");
        if (categoryIds == null || categoryIds.isEmpty()) throw new IllegalArgumentException("카테고리는 최소 1개 이상이어야 합니다.");

        this.id = id;
        this.storeId = storeId;
        this.name = name;
        this. price = price;
        this.categoryIds = Set.copyOf(categoryIds);
    }

    public static Product create(
            ProductId id,
            StoreId storeId,
            String name,
            Money price,
            Set<CategoryId> categoryIds
    ) {
        return new Product(id, storeId, name, price, categoryIds);
    }


    /**
     * Getter
     */
    public ProductId getId() { return id; }
    public StoreId getStoreId() { return storeId; }
    public String getName() { return name; }
    public Money getPrice() { return price; }
    public Set<CategoryId> getCategoryIds() { return categoryIds; }

    /**
     * 비지니스 로직
     */
     public void changePrice(Money price) {
         if (price == null) throw new IllegalArgumentException("가격은 필수입니다.");
         this.price = price;
     }
}
