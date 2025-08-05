package likelion13th.shop.DTO.response;

import likelion13th.shop.domain.Item;
import lombok.Getter;

@Getter
public class ItemResponse {
    private Long id;
    private String name;
    private int price;

    public static ItemResponse from(Item item) {
        ItemResponse resp = new ItemResponse();
        resp.id = item.getId();
        resp.name = item.getItem_name();
        resp.price = item.getPrice();
        return resp;
    }
}
// Item 엔티티로부터 조회된 정보를 클라이언트에 전달하기 위한 응답 DTO로, id, name, price 필드 포함
// 정적 팩토리 메서드 from(Item) 을 통해 엔티티를 DTO로 변환하는 역할을 수행
