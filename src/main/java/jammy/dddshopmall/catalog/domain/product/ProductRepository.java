package jammy.dddshopmall.catalog.domain.product;

import jammy.dddshopmall.store.domain.StoreId;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Optional<Product> findById(ProductId id);
    List<Product> findByStoreId(StoreId id);

    void save(Product product);
}