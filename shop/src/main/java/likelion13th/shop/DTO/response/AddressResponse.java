package likelion13th.shop.DTO.response;

import likelion13th.shop.domain.Address;
import lombok.Getter;

@Getter
public class AddressResponse {
    private String city;
    private String street;
    private String zipCode;

    public static AddressResponse from(Address addr) {
        AddressResponse resp = new AddressResponse();
        resp.city = addr.getAddressDetail();
        resp.street = addr.getAddress();
    resp.zipCode = addr.getZipcode();
        return resp;
    }
}
// Address 임베디드 값 객체를 기반으로 사용자 주소 정보를 응답하기 위한 DTO로, city, street, zipCode 필드 포함
// from(Address)를 통해 Address 객체를 응답 형식으로 변환하며 필드 매핑을 수행함

