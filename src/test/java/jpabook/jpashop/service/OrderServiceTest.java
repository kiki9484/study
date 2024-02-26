package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Item.Book;
import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.exception.NotEnoughException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문(){
        // given
        Member member = createMember();
        Book item = Book.createBook("book1", 10000, 10, "park", "111");

        em.persist(item);

        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // then
        Order order = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.ORDER, order.getStatus(), "상품 주문시 상태는 ORDER");
        assertEquals(1, order.getOrderItems().size(), "주문한 상품 종류 수가 정확해야 한다");
        assertEquals(10000 * orderCount, order.getTotalPrice(), "주문 가격은 가격 * 수량이다.");
        assertEquals(8, item.getStockQuantity(), "주문 수량만큼 상품의 재고가 줄어들어야 한다.");
    }

    @Test(expected = NotEnoughException.class)
    public void 상품주문_재고수량초과() {
        // given
        Member member = createMember();
        Book item = Book.createBook("book1", 10000, 10, "park", "111");

        em.persist(item);

        int orderCount = 11;

        // when
        orderService.order(member.getId(), item.getId(), orderCount); // NotEnoughException 예외 발생

        // then
        fail("재고 수량 부족 예외가 발생해야 한다.");
    }

    @Test
    public void 주문취소(){
        // given
        Member member = createMember();
        Book item = Book.createBook("book1", 10000, 10, "park", "111");

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);

        // then
        Order order = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCLE, order.getStatus(),"주문 취소시 상태는 CANCEL이다.");
        assertEquals(10, item.getStockQuantity(),"주문 취소시 재고가 원래대로 돌아와야 한다.");
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강북", "111-222"));
        em.persist(member);
        return member;
    }
}