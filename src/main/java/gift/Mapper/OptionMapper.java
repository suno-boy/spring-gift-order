package gift.Mapper;

import gift.DTO.OptionDTO;
import gift.Entity.OptionEntity;
import org.springframework.stereotype.Component;

@Component
public class OptionMapper {

    public OptionDTO toDTO(OptionEntity optionEntity) {
        OptionDTO optionDTO = new OptionDTO();
        optionDTO.setId(optionEntity.getId());
        optionDTO.setName(optionEntity.getName());
        optionDTO.setQuantity(optionEntity.getQuantity());
        optionDTO.setProductId(optionEntity.getProduct().getId());
        return optionDTO;
    }

    public OptionEntity toEntity(OptionDTO optionDTO) {
        OptionEntity optionEntity = new OptionEntity();
        optionEntity.setId(optionDTO.getId());
        optionEntity.setName(optionDTO.getName());
        optionEntity.setQuantity(optionDTO.getQuantity());
        return optionEntity;
    }
}
