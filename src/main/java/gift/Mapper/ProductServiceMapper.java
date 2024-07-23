package gift.Mapper;

import gift.DTO.ProductDTO;
import gift.DTO.WishDTO;
import gift.DTO.CategoryDTO;
import gift.DTO.OptionDTO;
import gift.Entity.*;
import gift.Repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProductServiceMapper {

    public ProductDTO convertToDTO(ProductEntity productEntity) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(productEntity.getId());
        productDTO.setName(productEntity.getName());
        productDTO.setPrice(productEntity.getPrice());
        productDTO.setImageUrl(productEntity.getImageUrl());
        productDTO.setWishes(convertWishesToDTOs(productEntity.getWishes()));
        productDTO.setCategory(convertToCategoryDTO(productEntity.getCategory()));
        productDTO.setOptions(convertOptionsToDTOs(productEntity.getOptions()));
        return productDTO;
    }

    private List<WishDTO> convertWishesToDTOs(List<WishEntity> wishEntities) {
        return Optional.ofNullable(wishEntities).orElse(List.of())
                .stream()
                .map(wishEntity -> new WishDTO(
                        wishEntity.getId(),
                        wishEntity.getUser().getId(),
                        wishEntity.getProduct().getId(),
                        wishEntity.getProductName()))
                .collect(Collectors.toList());
    }

    private CategoryDTO convertToCategoryDTO(CategoryEntity categoryEntity) {
        if (categoryEntity == null) {
            return null;
        }
        return new CategoryDTO(
                categoryEntity.getId(),
                categoryEntity.getName());
    }

    private List<OptionDTO> convertOptionsToDTOs(List<OptionEntity> optionEntities) {
        return Optional.ofNullable(optionEntities).orElse(List.of())
                .stream()
                .map(optionEntity -> new OptionDTO(
                        optionEntity.getId(),
                        optionEntity.getName(),
                        optionEntity.getQuantity(),
                        optionEntity.getProduct() != null ? optionEntity.getProduct().getId() : null))
                .collect(Collectors.toList());
    }

    public ResponseEntity<ProductEntity> findProductByIdResponse(Long id, ProductRepository productRepository) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
