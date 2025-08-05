package likelion13th.shop.repository;

import likelion13th.shop.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
// 카테고리 엔티티에 대한 기본 CRUD 기능을 JpaRepository를 통해 제공하는 인터페이스