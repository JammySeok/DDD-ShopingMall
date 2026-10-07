
## 이 프로젝트는 도메인 주도 설계의 방법론에 대해서 학습하기 위해 만들어진 토이 프로젝트입니다.


## 온라인 쇼핑몰 서비스

### 🛠 기술 스택

#### Language
![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=openjdk&logoColor=white)

#### ⚙ Framework & Library
![Spring Boot](https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/SpringDataJPA-6DB33F?style=for-the-badge&logo=hibernate&logoColor=white)


### 도메인 모델

| 컨텍스트 | 애그리거트 (루트) | 포함 | 참조 |
|---|---|---|---|
| order | Order | OrderLine, ShippingInfo, Orderer | MemberId, ProductId |
| member | Member | Address | - |
| store | Store | - | MemberId (상점 주인) |
| catalog | Product | - | StoreId, Set\<CategoryId\> |
| catalog | Category | - | - |

---

# 도메인 주도 설계(DDD)

## 개념

### 도메인(Domain) 
- 소프트웨어로 해결하려는 문제 영역
- ex) '온라인 쇼핑몰' 
  - 핵심 도메인 (Core): 서비스의 주된 기능 (ex. 주문 기능)
  - 지원 도메인 (Supporting): 핵심 도메인을 보조 (ex. 리뷰, 쿠폰 기능)
  - 일반 도메인 (Generic): 어느 서비스에나 있는 기능 (ex. 인증, 결제 같이 외부 솔루션으로 대체 가능한 것)

### 유비쿼터스 언어(Ubiquitous Language)
- 기획자와 개발자가 같이 쓰는 용어
- ex) 주문 취소 -> order.cancel()

### 바운디드 컨텍스트(BC: Bounded Context)
- 하나의 모델이 같은 의미로 통하는 경계
- ex) 카탈로그에서의 '상품'과 배송에서의 '상품'은 서로 다른 모델


## 코드 용어
- Entity: 고유 ID로 구별되고 상태가 바뀌는 객체 (JPA의 @Entity와 다른 개념)
- VO(Value Object): 값 자체가 의미 (불변 객체) (ex. Money, Address)
  - VO 쓰는 기준
    - 여러 값이 모여야 하나의 의미가 됨 (ex. 우편번호, 주소1, 주소2를 합쳐야 "주소"가 됨)
    - 자기만의 검증 규칙이 있음 (ex. 금액은 0 이상, 수량은 1 이상)
    - 자기만의 기능이 있음 (ex. `Money.add()`, `Address.isJeju()` (제주 배송비 추가))
    - 여러 곳에서 같은 개념으로 쓰임 (ex. 주소는 회원, 주문, 상점에서 모두 사용)
    - 타입을 구분해 실수를 막고 싶음 (ex. OrderNo와 ProductId를 바꿔 넣는 실수 방지)
- Aggregate: 함께 일관성을 지켜야 하는 객체 묶음
- Repository: 애그리거트 단위로 저장하고 조회하는 역할
- Domain Service: 특정 엔티티 하나에 넣기 애매한 도메인 로직 (ex. 할인 금액 계산)
- Domain Event: 도메인에서 일어난 사건. 다른 컨텍스트와 느슨하게 연결할 때 사용 (ex. "주문이 완료됨")
- Application Service: 유스케이스의 흐름 조율과 트랜잭션 담당


---

## 동작 흐름 (ex. 배송정보 수정)
```
① Controller
└ HTTP 요청을 받아 응용 서비스 호출

② Application Service  (@Transactional 시작)
├ (a) 요청 값으로 VO 조립
│     new ShippingInfo(new Receiver(...), new Address(...))
│     → 이 시점에는 값이 올바른지만 검사. 주문과는 아직 무관
│
├ (b) Repository로 Order 애그리거트 조회
│
└ (c) order.changeShippingInfo(newInfo)  ← 이 값으로 주문을 바꿔도 되는지 Order에게 "허락" 요청

③ Order (Aggregate root)
└ 규칙 검사: 취소되지 않았고, 출고 전인가?
    ├ 아니면 → 예외 (변경 거절)
    └ 맞으면 → this.shippingInfo = newInfo (객체 통째로 교체)

④ 트랜잭션 커밋 → 변경 내용이 DB에 반영
```

---

## 애그리거트 설계 기준

### 경계 나누기: B를 A 안에 넣을까?
하나라도 "분리" 쪽이면 분리한다. 애매하면 작게 나눈다(나중에 합치는게 편함).

| 질문 | 예 → |
|---|---|
| A가 삭제되면 B도 반드시 같이 사라지나? | 같이 |
| A의 규칙을 지키려면 B가 반드시 같이 바뀌어야 하나? | 같이 |
| B를 다른 사람이, 또는 다른 시점에 만드나? | 분리 |
| B만 따로 바뀌는 일이 흔한가? | 분리 |
| B를 여러 A가 공유하나? | 분리 |
| B가 끝없이 늘어날 수 있나? | 분리 |

### 참조 방향: 누가 누구의 ID를 들까?
- "X는 하나의 Y에 속한다" → X가 Y의 ID를 가진다 (ex. 리뷰는 하나의 상품에 속한다 → `Review.productId`)
- 1:N이면 N 쪽이 1의 ID를 가진다 (1 쪽은 목록을 들지 않는다)
- M:N이면 개수가 적은 쪽이 ID 목록을 가진다 (ex. `Product.categoryIds`)
- 한 방향으로만 참조한다


---

## DDD 설계 주의점

### 도메인 모델
- domain에 setter를 두지 않는다
  - 상태 변경은 의도가 드러나는 메서드로 한다 (ex. `cancel()`, `changeShippingInfo()`)
- 비즈니스 규칙은 엔티티 안에서 검사한다
  - 서비스에서 `if (order.getState() == ...)`로 상태를 검사하지 않는다
- 엔티티는 항상 유효한 상태여야 한다
  - 생성할 때와 변경할 때 모두 규칙(불변식)을 검사한다

### VO (Value Object)
- 불변으로 만들고, 생성 시점에 자기 값을 검증한다
- 값을 바꿀 때는 수정하지 않고 새 객체로 통째로 교체한다

### 애그리거트
- 애그리거트 내부 객체는 반드시 루트를 통해서만 변경한다
- 다른 애그리거트는 객체가 아니라 ID로 참조한다 (ex. `Product` → `ProductId`)
  - 그 시점에 확정되어야 하는 값(주문 당시 가격, 주문자 이름 등)은 복사해서 보관한다
- 하나의 트랜잭션에서는 하나의 애그리거트만 수정한다 (조회는 여러 개 가능)
- Repository는 애그리거트 루트 단위로만 만든다
  - 반대 방향 조회가 필요하면 리포지터리로 조회한다 (ex. `productRepository.findByStoreId()`)
- 생성에 다른 애그리거트의 상태가 필요하면, 그 애그리거트를 팩토리로 사용한다
  - ex) 영업 정지된 상점은 상품 등록 불가 → `store.createProduct()`

### 계층
- Application Service는 흐름 조율과 트랜잭션만 담당한다. 비즈니스 규칙을 넣지 않는다
- domain은 다른 계층에 의존하지 않는다 (DIP: 인터페이스는 domain, 구현은 infrastructure)
- 다른 바운디드 컨텍스트의 데이터에 직접 접근하지 않는다