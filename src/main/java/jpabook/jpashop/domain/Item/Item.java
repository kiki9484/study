package jpabook.jpashop.domain.Item;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 싱글 테이블 전략 사용
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public abstract class Item {
    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    /*
     * 도메인 주도 설계를 할 때, 엔터티 자체에서 해결할 수 있으면 엔터티 안에 넣는 것이 좋다(객체지향적 사고)
     * 1. addStock(int quantity): 재고 수량 더하기
     * 2. removeStock(int quantity): 재고 수량 줄이기
     */

    // stock 증가 메서드
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    // stock 감소 메서드
    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0) throw new NotEnoughException("need more stock");
        this.stockQuantity -= quantity;
    }
}
