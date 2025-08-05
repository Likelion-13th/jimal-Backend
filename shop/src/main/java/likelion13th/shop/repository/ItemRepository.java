package likelion13th.shop.repository;


import likelion13th.shop.domain.Category;
import likelion13th.shop.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByCategoryId(Long categoryId);
}
// 특정 카테고리에 속한 상품(Item) 목록을 조회하기 위해 findByCategories 메서드를 정의한 JPA 리포지토리