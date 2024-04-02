package sample.cafekiosk.api.controller.order;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static sample.cafekiosk.domain.product.ProductSellingStatus.HOLD;
import static sample.cafekiosk.domain.product.ProductSellingStatus.SELLING;

@ActiveProfiles("test")
class OrderCreateRequestTest {
    @DisplayName("주문 번호를 받아 요청 dto를 생성한다")
    @Test
    void OrderCreateRequest() {
        // given
        OrderCreateRequest orderCreateRequest = OrderCreateRequest.builder()
                .productNumbers(List.of("001", "002"))
                .build();

        // then
        assertThat(orderCreateRequest.getProductNumbers()).hasSize(2)
                .containsExactlyInAnyOrder("001", "002");
    }
}