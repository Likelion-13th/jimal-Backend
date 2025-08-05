package likelion13th.shop.DTO.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddressRequest {

    @NotBlank(message = "도시는 필수 입력값입니다.")
    private String city;

    @NotBlank(message = "도로명 주소는 필수 입력값입니다.")
    private String street;

    @NotBlank(message = "우편번호는 필수 입력값입니다.")
    private String zipCode;

    public AddressRequest(String city, String street, String zipCode) {
        this.city = city;
        this.street = street;
        this.zipCode = zipCode;
    }
}
// 사용자 주소 등록 및 수정 요청 시 사용되는 DTO로, 도시, 도로명 주소, 우편번호를 필수값으로 검증

