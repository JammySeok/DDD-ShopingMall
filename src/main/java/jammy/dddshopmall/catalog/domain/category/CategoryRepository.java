package jammy.dddshopmall.catalog.domain.category;

import java.util.Optional;

public interface CategoryRepository {

    Optional<Category> findById(CategoryId id);
    void save(Category category);
}