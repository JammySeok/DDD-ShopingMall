package jammy.dddshopmall.catalog.domain.category;

public class Category {

    private CategoryId id;
    private String name;

    private Category(
            CategoryId id,
            String name
    ) {

        if (id == null) throw new IllegalArgumentException("카테고리 아이디는 필수입니다.");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("이름은 필수입니다.");

        this.id = id;
        this.name = name;
    }

    public static Category create(
            CategoryId id,
            String name
    ) {
        return new Category(id, name);
    }


    /**
     * Getter
     */
    public CategoryId getId() { return id; }
    public String getName() { return name; }


    /**
     * 비지니스 로직
     */
    public void changeName(
            String name
    ) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("이름은 필수입니다.");
        this.name = name;
    }
}