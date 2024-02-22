package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    // Item 저장 메서드
    public void save(Item item) {
        if(item.getId() == null) em.persist(item); // id값이 없으면 새로 생성하는 객체라는 뜻이다.
        else em.merge(item); // id값이 있으면 이미 db에 등록되어 있는 것을 가져오는 것이다.(update처럼 이해하면 된다)
    }

    // id로 Item 찾기
    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    // 전체 Item 찾기
    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
