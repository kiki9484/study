package sample.cafekiosk.spring.domain.product;

import jakarta.persistence.*;
import lombok.*;
import sample.cafekiosk.spring.domain.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@ToString
public class Product extends BaseEntity {
    @Id @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private String productNumber;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    @Enumerated(EnumType.STRING)
    private ProductSellingStatus sellingStatus;

    private String name;

    private int price;

    // id를 제외한 필드로 빌더를 만들어준다.
    @Builder
    private Product(String productNumber, ProductType type, ProductSellingStatus sellingStatus, String name, int price) {
        this.productNumber = productNumber;
        this.type = type;
        this.sellingStatus = sellingStatus;
        this.name = name;
        this.price = price;
    }
}
