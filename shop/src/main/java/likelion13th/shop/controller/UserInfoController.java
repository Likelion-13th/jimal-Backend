package likelion13th.shop.controller;

import likelion13th.shop.DTO.response.UserInfoResponse;
import likelion13th.shop.DTO.response.UserMileageResponse;
import likelion13th.shop.DTO.response.AddressResponse;
import likelion13th.shop.login.auth.jwt.CustomUserDetails;
import likelion13th.shop.service.UserService;
import likelion13th.shop.global.api.SuccessCode;
import likelion13th.shop.service.UserAddressService;
import likelion13th.shop.global.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserInfoController {
    private final UserService userService;
    private final UserAddressService userAddressService;

    //정보 조회
    @Operation(summary = "내 정보 조회", description = "로그인한 사용자의 정보를 조회합니다.")
    @GetMapping("/profile")
    public ApiResponse<?> getProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserInfoResponse response = userService.getUserInfo(userDetails.getUser());
        return ApiResponse.onSuccess(SuccessCode.USER_INFO_GET_SUCCESS,response);
    }

    //내 마일리지 조회
    @Operation(summary = "내 마일리지 조회", description = "로그인한 사용자의 마일리지를 조회합니다.")
    @GetMapping("/mileage")
    public ApiResponse<?> getMileage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        UserMileageResponse response = userService.getUserMileage(userDetails.getUser());
        return ApiResponse.onSuccess(SuccessCode.USER_MILEAGE_GET_SUCCESS, response);
    }

    //내 주소 조회
    @Operation(summary = "내 주소 조회", description = "로그인한 사용자의 주소를 조회합니다.")
    @GetMapping("/address")
    public ApiResponse<?> getAddress(@AuthenticationPrincipal CustomUserDetails userDetails) {
        AddressResponse response = userAddressService.getAddress(userDetails.getUser());
        return ApiResponse.onSuccess(SuccessCode.ADDRESS_GET_SUCCESS, response);
    }
}
// 로그인한 사용자의 정보, 마일리지, 주소를 조회하는 사용자 정보 전용 API 컨트롤러로 구성함
// 컨트롤러 계층에서는 서비스 분리 원칙(Service Separation Pattern)을 적용해 책임을 역할별로 위임
// @AuthenticationPrincipal을 통해 인증된 사용자 정보를 받아 각 기능(Service)을 통해 응답 데이터를 생성


