package likelion13th.shop.service;

import likelion13th.shop.DTO.request.CategoryRequest;
import likelion13th.shop.DTO.response.CategoryResponse;
import likelion13th.shop.DTO.response.ItemResponse;
import likelion13th.shop.global.api.ErrorCode;
import likelion13th.shop.domain.Category;
import likelion13th.shop.global.exception.GeneralException;
import likelion13th.shop.repository.CategoryRepository;
import likelion13th.shop.repository.ItemRepository;
import likelion13th.shop.domain.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

    //개별 카테고리 조회
    public CategoryResponse getCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new GeneralException(ErrorCode.CATEGORY_NOT_FOUND));
        return new CategoryResponse(category.getId(), category.getName());
    }

    // 카테고리 생성
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        Category savedCategory = categoryRepository.save(category);

        return new CategoryResponse(
                savedCategory.getId(),
                savedCategory.getName());
    }

    //카테고리 삭제
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다."));

        categoryRepository.deleteById(id);
    }

    //카테고리별 상품 조회
    public List<ItemResponse> getItemsByCategory(Long categoryId) {
        List<Item> items = itemRepository.findAllByCategoryId(categoryId);
        return items.stream()
                .map(ItemResponse::from)
                .collect(Collectors.toList());
    }

    //카테고리 전체 조회
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
    }
}