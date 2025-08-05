package likelion13th.shop.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@Getter
public class Address {

    @Column(nullable = false)
    private String zipcode;

    @Column(nullable = false)
    private String address; //

    @Column(nullable = false)
    private String addressDetail; //

    public Address() {
        this.zipcode = "10540";
        this.address = "경기도 고양시 덕양구 항공대학로 76";
        this.addressDetail = "한국항공대학교";
    }
}

// JPA의 @Embeddable을 사용한 값 타입 객체로, 사용자 주소 정보를 구성하는 zipcode, address, addressDetail 필드 포함
// 기본 생성자에 디폴트 주소 값을 설정하여 테스트 및 초기화 시 사용 가능하도록 설계