package jammy.dddshopmall.order.domain;

import jammy.dddshopmall.common.model.Address;

// VO(Value Object)
public record ShippingInfo(
        Receiver receiver,
        Address address
) {

    public ShippingInfo {
        if (receiver == null || address == null) {
            throw new IllegalArgumentException("수령인과 주소는 필수입니다.");
        }
    }

    public static ShippingInfo of(Receiver receiver, Address address) {
        return new ShippingInfo(receiver, address);
    }
}