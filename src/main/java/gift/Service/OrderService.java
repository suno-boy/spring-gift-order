package gift.Service;

import gift.DTO.OrderRequestDTO;
import gift.DTO.OrderResponseDTO;
import gift.DTO.OptionDTO;
import gift.Entity.OptionEntity;
import gift.Entity.OrderEntity;
import gift.Entity.ProductEntity;
import gift.Entity.WishEntity;
import gift.Repository.OrderRepository;
import gift.Repository.OptionRepository;
import gift.Repository.ProductRepository;
import gift.Repository.WishRepository;
import gift.Mapper.OptionMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OptionService optionService;

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private OptionMapper optionMapper;


    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        ProductEntity product = productRepository.findById(orderRequestDTO.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));
        OptionEntity option = optionRepository.findById(orderRequestDTO.getOptionId()).orElseThrow(() -> new RuntimeException("Option not found"));

        OptionDTO optionDTO = optionMapper.toDTO(option);

        // 옵션 수량 차감
        optionService.subtractQuantity(orderRequestDTO.getOptionId(), orderRequestDTO.getQuantity(), optionDTO);

        OrderEntity order = new OrderEntity(product, option, orderRequestDTO.getQuantity());
        order = orderRepository.save(order);

        // 위시리스트에서 해당 상품 삭제
        List<WishEntity> wishes = wishRepository.findByProductId(orderRequestDTO.getProductId());
        for (WishEntity wish : wishes) {
            wishRepository.delete(wish);
        }

        return new OrderResponseDTO(order.getId(), "주문 성공", "주문 성공");
    }
}
