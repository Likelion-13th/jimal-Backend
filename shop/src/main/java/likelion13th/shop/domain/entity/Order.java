package likelion13th.shop.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 예시 필드들 (필요에 따라 수정해줘)
    private String orderNumber;
    private String status;

    // 기본 생성자 (JPA는 꼭 필요해!)
    public Order() {
    }

    public Order(String orderNumber, String status) {
        this.orderNumber = orderNumber;
        this.status = status;
    }
}

