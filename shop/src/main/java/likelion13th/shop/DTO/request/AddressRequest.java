package likelion13th.shop.DTO.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddressRequest {
    private String zipCode;
    private String address;       // 사용자가 변경 가능
    private String addressDetail;
}
// 사용자 주소 등록 및 수정 요청 시 사용되는 DTO로, 도시, 도로명 주소, 우편번호를 필수값으로 검증

