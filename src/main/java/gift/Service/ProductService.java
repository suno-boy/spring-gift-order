package gift.Service;

import gift.DTO.ProductDTO;
import gift.Entity.ProductEntity;
import gift.Repository.ProductRepository;
import gift.Mapper.ProductServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductServiceMapper productServiceMapper;

    public List<ProductEntity> findAllProducts() {
        return productRepository.findAll();
    }

    public Optional<ProductEntity> findProductById(Long id) {
        return productRepository.findById(id);
    }

    public ProductEntity saveProduct(ProductEntity productEntity) {
        return productRepository.save(productEntity);
    }

    public ProductEntity updateProduct(Long id, ProductEntity productEntity) {
        Optional<ProductEntity> existingProductOption = productRepository.findById(id);
        if (existingProductOption.isPresent()) {
            ProductEntity existingProduct = existingProductOption.get();
            existingProduct.setName(productEntity.getName());
            existingProduct.setPrice(productEntity.getPrice());
            existingProduct.setImageUrl(productEntity.getImageUrl());
            existingProduct.setCategory(productEntity.getCategory());
            existingProduct.setWishes(productEntity.getWishes());
            existingProduct.setOptions(productEntity.getOptions());
            return productRepository.save(existingProduct);
        } else {
            throw new RuntimeException("변경하려는 상품이 존재하지 않습니다.");
        }
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public Page<ProductDTO> getProducts(Pageable pageable) {
        Page<ProductEntity> productPage = productRepository.findAll(pageable);
        return productPage.map(productServiceMapper::convertToDTO);
    }

    public ResponseEntity<ProductEntity> findProductByIdResponse(Long id) {
        return productServiceMapper.findProductByIdResponse(id, productRepository);
    }
}
