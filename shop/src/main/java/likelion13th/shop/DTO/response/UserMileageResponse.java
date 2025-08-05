package likelion13th.shop.DTO.response;

import likelion13th.shop.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserMileageResponse {
    private int mileage;

    public static UserMileageResponse from(User user) {
        return new UserMileageResponse(user.getMaxMileage());
    }
}
// 사용자 보유 마일리지를 응답하기 위한 DTO로, User 엔티티의 maxMileage 값을 기반으로 구성됨
// 정적 메서드 from(User)를 통해 엔티티에서 마일리지 값을 추출하여 DTO로 변환함

