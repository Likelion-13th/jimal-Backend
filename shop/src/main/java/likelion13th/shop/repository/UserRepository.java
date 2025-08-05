package likelion13th.shop.repository;

import likelion13th.shop.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // user_id 기반 사용자 찾기 (feature/4)
    Optional<User> findById(Long userId);

    boolean existsById(Long userId);

    // providerId(카카오 고유 ID) 기반 조회 (feature/4)
    Optional<User> findByProviderId(String providerId);

    boolean existsByProviderId(String providerId);

    // usernickname(닉네임) 기반 사용자 찾기 (develop)
    List<User> findByUsernickname(String usernickname);

    // 향후 필요 시 사용할 수 있도록 주석 유지
    //Optional<User> findByKakaoId(String kakaoId);
}
// 사용자 정보를 조회하거나 존재 여부를 확인하기 위해 사용자 식별자, 소셜 ID, 닉네임 등을 기준으로 다양한 메서드를 정의함
// Spring Data JPA의 메서드 네이밍 규칙을 활용해 쿼리 없이도 자동으로 데이터 접근 로직을 처리할 수 있도록 구성함
// 기능 단위별 주석을 통해 브랜치 또는 향후 개발 이력을 명시하며 확장 가능성을 고려한 설계를 적용함

