package jammy.dddshopmall.catalog.domain.category;

public record CategoryId (
        String id
) {

    public CategoryId {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("카테고리 아이디는 필수입니다.");
    }

    public static CategoryId of(String id) {
        return new CategoryId(id);
    }
}