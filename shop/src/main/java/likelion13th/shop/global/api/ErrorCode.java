package likelion13th.shop.global.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode implements BaseCode {
    ITEM_NOT_FOUND("E001", "해당 상품을 찾을 수 없습니다.");

    private final String code;
    private final String reason;
}