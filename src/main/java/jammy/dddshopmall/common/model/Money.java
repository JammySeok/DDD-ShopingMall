package jammy.dddshopmall.common.model;

// VO(Value Object)
public record Money(
        int value
) {

    public Money {
        if (value < 0) throw new IllegalArgumentException("금액은 0 이상이어야 합니다");
    }

    public static Money of(int value) { return new Money(value); }

    public Money add(Money other) {
        return new Money(this.value + other.value);
    }

    public Money multiply(int multiplier) {
        return new Money(this.value * multiplier);
    }
}
