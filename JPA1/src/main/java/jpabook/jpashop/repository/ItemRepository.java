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
        /*
         * 병합 사용 - merge
         * 병합은 준영속 상태의 엔티티를 영속 상태로 변경할 때 사용하는 기능이다.
         *
         * @Transactional
         * public Item update(Long itemId, Book param){
         *     Item findItem = itemRepository.findOne(itemId);
         *     findItem.setPrice(param.getPrice());
         *     findItem.setName(param.getName());
         *     findItem.setStockQuantity(param.getStockQuantity());
         *     return findItem;
         * }
         *
         * merge에서 넘긴 파라미터 값으로 모든 데이터를 바꿔치기 하는 것이다.
         * 바꿔치기를 했기 때문에 transaction을 commit할 때 반영이 되는 것이다.
         * 결국 위의 코드를 merge()를 사용하면 JPA가 대신 해주는 것이다.
         * item이 영속 상태가 되는 것이 아니라 em.merge(item)을 해서 리턴된 값이 영속 상태가 되는 것이다.
         *
         * 주의! 변경 감지 기능을 사용하면 원하는 속성만 변경할 수 있지만, 병합을 사용하게 되면 모든 속성을 바꾸게 된다.
         * 병합 시 값이 없으면 null로 업데이트할 가능성이 있으므로 조심해야 한다.(병합은 모든 필드를 교체한다)
         * 그러므로, merge보단 변경 감지를 쓰는 것을 더 추천한다!
         */

        if(item.getId() == null) em.persist(item); // id값이 없으면 새로 생성하는 객체라는 뜻이다.
        else {
            Item merge = em.merge(item); // id값이 있으면 이미 db에 등록되어 있는 것을 가져오는 것이다.(update처럼 이해하면 된다)
        }
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
