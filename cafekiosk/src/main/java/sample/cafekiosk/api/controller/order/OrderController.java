package sample.cafekiosk.api.controller.order;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import sample.cafekiosk.api.service.order.OrderService;

@RequiredArgsConstructor
@RestController
public class OrderController {
    private final OrderService orderService;


}
