package jpabook.jpashop.service;

import jpabook.jpashop.domain.Item.Book;
import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    /*
     * 변경 감지 방법(dirty checking) - 이 방법 추천!
     * itemRepository.save(), merge, entityManager.persist() 모두 호출할 필요가 없다.
     * findItem으로 찾아온 Item은 영속 상태이다.
     * 값을 세팅한 다음엔 Transaction이 commit이 돼서 JPA가 flush()를 한다.
     * JPA는 flush()를 통해 엔터티 컨텍스트에서 변경된 것을 모두 찾는다. 찾아서 바뀐 값을 업데이트해서 db를 변경한다.
     */
    /*@Transactional
    public Item update(Long itemId, Book param) {
        Item findItem = itemRepository.findOne(itemId);
        findItem.setPrice(param.getPrice());
        findItem.setName(param.getName());
        findItem.setStockQuantity(param.getStockQuantity());
        return findItem;
    }*/

    @Transactional
    public void update(Long itemId, UpdateItemDto itemDto) {
        Item findItem = itemRepository.findOne(itemId);
        findItem.setName(itemDto.getName());
        findItem.setPrice(itemDto.getPrice());
        findItem.setStockQuantity(itemDto.getStockQuantity());
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }
}
