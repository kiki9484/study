package sample.cafekiosk.spring.api.controller.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.api.controller.order.OrderCreateRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

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