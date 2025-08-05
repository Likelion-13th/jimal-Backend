package likelion13th.shop.DTO.response;

import likelion13th.shop.domain.User;
import lombok.Getter;

@Getter
public class UserInfoResponse {
    private Long id;
    private String name;
    private String email;

    public static UserInfoResponse from(User user) {
        UserInfoResponse resp = new UserInfoResponse();
        resp.id = user.getId();
        resp.name = user.getUsernickname();
        resp.email = user.getAddress().getZipcode();
        return resp;
    }
}
// User 엔티티에서 추출한 사용자 정보를 담는 응답 DTO로, id, 이름, 우편번호( email 필드에 매핑)를 포함
// from(User)를 통해 User 객체를 UserInfoResponse로 변환하는 역할 수행