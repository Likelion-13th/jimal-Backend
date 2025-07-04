package likelion13th.shop.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키 자동 생성 (MySQL auto_increment와 유사)
    private Long id;

    // 예시 필드
    private String name;

    // 기본 생성자 (JPA는 기본 생성자가 꼭 필요해!)
    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }
}
