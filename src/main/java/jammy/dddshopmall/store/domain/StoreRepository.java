package jammy.dddshopmall.store.domain;

import java.util.Optional;

public interface StoreRepository {

    Optional<Store> findById(StoreId id);
    void save(Store store);
}
