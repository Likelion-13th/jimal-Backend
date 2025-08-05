package likelion13th.shop.service;

import likelion13th.shop.DTO.response.UserInfoResponse;
import likelion13th.shop.DTO.response.UserMileageResponse;
import likelion13th.shop.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    //사용자 정보 조회
    public UserInfoResponse getUserInfo(User user) {
        return UserInfoResponse.from(user);
    }

    public UserMileageResponse getUserMileage(User user) {
        return UserMileageResponse.from(user);
    }
}
