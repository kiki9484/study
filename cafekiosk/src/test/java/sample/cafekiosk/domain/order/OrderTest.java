package sample.cafekiosk.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.domain.product.Product;
import sample.cafekiosk.domain.product.ProductType;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static sample.cafekiosk.domain.order.OrderStatus.*;
import static sample.cafekiosk.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.domain.product.ProductType.HANDMADE;

@ActiveProfiles("test")
class OrderTest {
    @DisplayName("주문 생성 시 주문 상태는 INIT이다.")
    @Test
    void init(){
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        List<Product> products = createProducts();

        // when
        Order order = Order.createOrder(products, registeredDateTime);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(INIT);
    }

    @DisplayName("주문 생성 시 상품 리스트에서 주문의 총 금액을 계산한다.")
    @Test
    void calculateTotalPrcie(){
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        List<Product> products = createProducts();

        // when
        Order order = Order.createOrder(products, registeredDateTime);

        // then
        assertThat(order.getTotalPrice()).isEqualTo(3000);
    }

    @DisplayName("주문 생성 시 주문 등록 시간을 기록한다.")
    @Test
    void registeredDateTime(){
        // given
        LocalDateTime registeredDateTime = LocalDateTime.now();
        List<Product> products = createProducts();

        // when
        Order order = Order.createOrder(products, registeredDateTime);

        // then
        assertThat(order.getRegisteredDateTime()).isEqualTo(registeredDateTime);
    }

    private List<Product> createProducts() {
        return List.of(
                Product.createProduct("001", HANDMADE, SELLING, "이름1", 1000),
                Product.createProduct("002", HANDMADE, SELLING, "이름2", 2000));
    }
}