package sample.cafekiosk.api.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.api.service.product.request.ProductCreateServiceRequest;
import sample.cafekiosk.api.service.product.response.ProductResponse;
import sample.cafekiosk.domain.product.Product;
import sample.cafekiosk.domain.product.ProductRepository;
import sample.cafekiosk.domain.product.ProductSellingStatus;
import sample.cafekiosk.domain.product.ProductType;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static sample.cafekiosk.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.domain.product.ProductType.HANDMADE;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    // 조회를 했을 경우 [SELLING, HOLD] 제품만 가져온다.
    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(forDisplay());
        return products.stream()
                .map(ProductResponse::of)
                .collect(toList());
    }

    @Transactional
    public ProductResponse createProduct(ProductCreateServiceRequest request) {
        // productNumber 부여하기 -> DB에서 마지막 저장된 Product의 상품 번호를 읽어와서 +1
        String nextProductNumber = createNextProductNumber();

        Product product = request.toEntity(nextProductNumber);
        Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);
    }

    private String createNextProductNumber() {
        String latestProductNumber = productRepository.findLatestProductNumber();
        if(latestProductNumber == null) return "001";

        int latestProductNumberInt = Integer.parseInt(latestProductNumber);
        int nextProductNumberInt = latestProductNumberInt + 1;
        return String.format("%03d", nextProductNumberInt);
    }
}
