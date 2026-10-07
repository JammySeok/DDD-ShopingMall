package jammy.dddshopmall.store.domain;

public record StoreId(
        String id
) {
    
    public StoreId { 
        if (id == null) throw new IllegalArgumentException("상점 아이디는 필수입니다.");
    }
}
