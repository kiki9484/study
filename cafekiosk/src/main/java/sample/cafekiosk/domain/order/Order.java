package sample.cafekiosk.domain.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sample.cafekiosk.domain.BaseEntity;
import sample.cafekiosk.domain.orderproduct.OrderProduct;
import sample.cafekiosk.domain.product.Product;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static sample.cafekiosk.domain.order.OrderStatus.INIT;

@Entity
@Table(name = "orders") // 테이블 명으로 "order"가 불가능하다. "orders"로 바꿔준다.
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Order extends BaseEntity {
    @Id @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private int totalPrice;
    private LocalDateTime registeredDateTime;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @Builder
    private Order(List<Product> products, LocalDateTime registeredDateTime) {
        this.orderStatus = INIT;
        this.totalPrice = calculateTotalPrice(products);
        this.registeredDateTime = registeredDateTime;
        this.orderProducts = products.stream()
                .map(product -> new OrderProduct(this, product))
                .collect(toList());
    }

    public static Order createOrder(List<Product> products, LocalDateTime registeredDateTime) {
        return Order.builder()
                .products(products)
                .registeredDateTime(registeredDateTime)
                .build();
    }

    private int calculateTotalPrice(List<Product> products) {
        return products.stream()
                .mapToInt(Product::getPrice)
                .sum();
    }

}

