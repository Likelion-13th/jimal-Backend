package likelion13th.shop.DTO.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import likelion13th.shop.domain.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponse {
    private Long id;
    private String name;
    private int price;
    private String brand;
    private String imagePath;
    private boolean isNew;

    @JsonProperty("isNew")
    public boolean getIsNew() {
        return isNew;
    }

    // Item → ItemResponseDto 변환
    public static ItemResponse from(Item item) {
        return new ItemResponse(
                item.getId(),
                item.getItem_name(),
                item.getPrice(),
                item.getBrand(),
                item.getImagePath(),
                item.isNew()
        );
    }
    /** Order과 일대다 연관관계 설정
     * -> Item에서 Order의 목록을 볼 일이 없으므로 단방향 처리 **/
}
// Item 엔티티로부터 조회된 정보를 클라이언트에 전달하기 위한 응답 DTO로, id, name, price 필드 포함
// 정적 팩토리 메서드 from(Item) 을 통해 엔티티를 DTO로 변환하는 역할을 수행
