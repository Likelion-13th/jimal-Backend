package likelion13th.shop.DTO.response;

import likelion13th.shop.domain.User;
import likelion13th.shop.domain.Order;
import likelion13th.shop.global.constant.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class UserInfoResponse {
    private String usernickname;
    private int recentTotal;
    private int maxMileage;
    private Map<OrderStatus, Integer> orderStatusCounts;

    public static UserInfoResponse from(User user) {
        Map<OrderStatus, Integer> orderStatusCounts = user.getOrders().stream()
                .collect(Collectors.groupingBy(
                        Order::getStatus,
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ));

        // PROCESSING, COMPLETE, CANCEL 상태가 없는 경우 0으로 초기화
        orderStatusCounts.putIfAbsent(OrderStatus.PROCESSING, 0);
        orderStatusCounts.putIfAbsent(OrderStatus.COMPLETE, 0);
        orderStatusCounts.putIfAbsent(OrderStatus.CANCEL, 0);

        return new UserInfoResponse(
                user.getUsernickname(),
                user.getRecentTotal(),
                user.getMaxMileage(),
                orderStatusCounts
        );
    }
}
// User 엔티티에서 추출한 사용자 정보를 담는 응답 DTO로, id, 이름, 우편번호( email 필드에 매핑)를 포함
// from(User)를 통해 User 객체를 UserInfoResponse로 변환하는 역할 수행