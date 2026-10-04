package jammy.dddshopmall.catalog.domain;

// VO(Value Object)
public record ProductId (
        String id
) {

    public ProductId {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("ID는 필수입니다.");
        }
    }

    public static ProductId of(String id) {
        return new ProductId(id);
    }
}