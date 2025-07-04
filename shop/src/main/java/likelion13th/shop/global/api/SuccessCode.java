package likelion13th.shop.global.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SuccessCode implements BaseCode {
    ITEM_GET_SUCCESS("S001", "상품 조회 성공");

    private final String code;
    private final String reason;
}
