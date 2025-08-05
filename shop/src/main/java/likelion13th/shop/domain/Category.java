package likelion13th.shop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import likelion13th.shop.domain.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Item> items = new ArrayList<>();

    public void setName(String name) {
        this.name = name;
    }
}
// 카테고리 정보를 나타내는 엔티티로, 고유한 name 필드와 다대일 연관관계를 가지는 Item 리스트를 포함
// BaseEntity를 상속받아 생성/수정 시간 자동 관리, 연관된 상품은 cascade 및 orphanRemoval로 함께 관리됨
