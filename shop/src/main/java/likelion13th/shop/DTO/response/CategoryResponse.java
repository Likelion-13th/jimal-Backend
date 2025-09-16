package likelion13th.shop.DTO.response;

import jakarta.validation.constraints.NotBlank;
import likelion13th.shop.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryResponse {
    private long id;
    
    private String name;

    public static CategoryResponse from(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }

}

