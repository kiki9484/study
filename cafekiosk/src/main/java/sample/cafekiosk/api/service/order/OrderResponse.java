package sample.cafekiosk.api.service.order;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import sample.cafekiosk.api.service.product.response.ProductResponse;
import sample.cafekiosk.domain.order.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode
@ToString
public class OrderResponse{
    private final Long id;
    private final int totalPrice;
    private final LocalDateTime registeredDateTime;
    private final List<ProductResponse> products;

    @Builder
    private OrderResponse(Long id, int totalPrice, LocalDateTime registeredDateTime, List<ProductResponse> products){
        this.id = id;
        this.totalPrice = totalPrice;
        this.registeredDateTime = registeredDateTime;
        this.products = products;

    }

    public static OrderResponse of(Order order){
        return OrderResponse.builder()
                .id(order.getId())
                .totalPrice(order.getTotalPrice())
                .registeredDateTime(order.getRegisteredDateTime())
                .products(getProductResponses(order))
                .build();
    }

    private static List<ProductResponse> getProductResponses(Order order) {
        return order.getOrderProducts().stream()
                .map(orderProduct -> ProductResponse.of(orderProduct.getProduct()))
                .collect(Collectors.toList());
    }
}
