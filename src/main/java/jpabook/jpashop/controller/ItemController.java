package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Item.Book;
import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form) {
        Book book = Book.createBook(form.getName(), form.getPrice(), form.getStockQuantity(), form.getAuthor(), form.getIsbn());
        itemService.saveItem(book);
        return "redirect:/items";
    }

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable Long itemId, Model model) {
        Book item = (Book)itemService.findOne(itemId);
        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    /*
     * Book 객체는 이미 DB에 한번 저장되어서 식별자가 존재한다.
     * 이렇게 임의로 만들어낸 엔티티도 기존 식별자를 가지고 있으면 준영속 엔티티로 볼 수 있다.
     * JPA가 관리하는 영속 엔터티는 변경 감지가 일어나서 트랜잭션 커밋 시점에 변경되었다는 것을 알고 바꿔치기를 한다.
     * 여기에서 Book은 JPA가 관리하고 있지 않으므로 Book의 상태를 바꾸어도 db에서의 변경이 일어나지 않는다.
     *
     * 준영속 엔티티를 수정하는 2가지 방법
     * 1. 변경 감지(dirty checking) 기능 사용
     * 2. 병합(merge) 사용
     */
    @PostMapping("/items/{itemId}/edit")
    public String updateItem(Long itemId, @ModelAttribute("form") BookForm form) {
        Book book = Book.createBook(itemId, form.getName(), form.getPrice(), form.getStockQuantity(), form.getAuthor(), form.getIsbn());
        itemService.saveItem(book);
        return "redirect:/items";
    }
}
