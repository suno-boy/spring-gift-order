package gift.Mapper;

import gift.DTO.WishDTO;
import gift.Entity.WishEntity;
import gift.Repository.WishRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class WishServiceMapper {

    private WishRepository wishRepository;

    public ResponseEntity<WishEntity> findWishByIdResponse(Long id) {
        return wishRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public WishDTO convertToDTO(WishEntity wishEntity) {
        return new WishDTO(
                wishEntity.getId(),
                wishEntity.getUser().getId(),
                wishEntity.getProduct().getId(),
                wishEntity.getProductName()
        );
    }
}
