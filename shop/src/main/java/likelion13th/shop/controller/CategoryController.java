package likelion13th.shop.controller;

import likelion13th.shop.DTO.request.CategoryRequest;
import likelion13th.shop.DTO.response.CategoryResponse;
import likelion13th.shop.DTO.response.ItemResponse;
import likelion13th.shop.global.api.SuccessCode;
import likelion13th.shop.service.CategoryService;
import likelion13th.shop.global.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    //카테고리 생성
    @PostMapping
    @Operation(summary = "카테고리 생성", description = "새로운 카테고리를 생성합니다.")
    public ApiResponse<?> createCategory(
            @RequestBody CategoryRequest request
    ) {
        CategoryResponse created = categoryService.createCategory(request);
        return ApiResponse.onSuccess(SuccessCode.CREATED, created);
    }

    //카테코리 별 상품 조회
    @GetMapping("/{categoryId}/items")
    @Operation(summary = "카테고리 별 상품 조회", description = "카테고리 별 상품을 조회합니다.")
    public ApiResponse<?> getItemsByCategory(@PathVariable Long categoryId) {
        List<ItemResponse> items = categoryService.getItemsByCategory(categoryId);
        return ApiResponse.onSuccess(SuccessCode.CATEGORY_ITEMS_GET_SUCCESS, items);
    }

    //모든 카테고리 조회
    @GetMapping
    @Operation(summary = "모든 카테고리 조회", description = "모든 카테고리를 조회합니다.")
    public ApiResponse<?> getAllCategories() {
        List<CategoryResponse> categories = categoryService.getAllCategories();
        return ApiResponse.onSuccess(SuccessCode.CATEGORY_ITEMS_GET_SUCCESS, categories);
    }

    //카테고리 삭제
    @DeleteMapping("/{categoryId}")
    @Operation(summary = "카테고리 취소", description = "카테고리를 삭제합니다.")
    public ApiResponse<?> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ApiResponse.onSuccess(SuccessCode.OK, "카테고리가 삭제되었습니다.");
    }
}
// Order API 패턴을 참고하여 카테고리 생성, 조회, 삭제 기능을 구현
// @Operation 어노테이션을 사용해 Swagger 문서화 처리
// ApiResponse와 SuccessCode를 활용해 일관된 API 응답 구조 유지



