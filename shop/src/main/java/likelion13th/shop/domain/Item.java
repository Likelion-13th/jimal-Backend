package likelion13th.shop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import likelion13th.shop.domain.entity.BaseEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "item")
@NoArgsConstructor
@AllArgsConstructor
public class Item extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @Column(nullable = false)
    private String item_name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String imagePath;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    @Setter
    private boolean isNew= false;

    //Category와 다대다 연관관계 설정
    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Order> orders = new ArrayList<>();

    public Item(String item_name, int price, String thumbnail_img, String brand, boolean isNew) {
        this.item_name = item_name;
        this.price = price;
        this.imagePath = imagePath;
        this.brand = brand;
        this.isNew= false;
    }
}
// 상품 정보를 나타내는 엔티티로, 이름, 가격, 브랜드, 이미지 경로, 신상품 여부 등의 필드를 포함
// 카테고리와는 다대다, 주문과는 일대다 연관관계를 설정하여 상품 기반의 연관 데이터를 관리할 수 있도록 구성
