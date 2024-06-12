package hello.itemservice.web.form;

import hello.itemservice.domain.item.DeliveryCode;
import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.ItemType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/form/items")
@RequiredArgsConstructor
public class FormItemController {

    private final ItemRepository itemRepository;

    /**
     * 상품 등록
     * th:object를 적용하려면 오브젝트 정보를 넘겨주어야 한다.
     * 등록 폼이기 때문에 데이터가 비어있는 빈 오브젝트를 만들어서 뷰에 넘겨준다.
     * @param model
     * @return view name
     */
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "form/addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        log.info("item.open={}", item.getOpen());
        log.info("item.regions={}", item.getRegions());
        log.info("item.itemType={}", item.getItemType());

        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/form/items/{itemId}";
    }

    /**
     * 상품 수정
     * item 정보를 가져와서 수정화면을 보여줄 때 사용한다.
     * @param itemId : 상품의 아이디
     * @param model
     * @return view name
     */
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);

        model.addAttribute("item", item);
        return "form/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);

        log.info("item.open={}", item.getOpen());
        log.info("item.regions={}", item.getRegions());
        log.info("item.itemType={}", item.getItemType());
        return "redirect:/form/items/{itemId}";
    }

    /**
     * 단일 item 보여주기
     * @param itemId
     * @param model
     * @return view name
     */
    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "form/item";
    }

    /**
     * 상품 전체 목록 보여주기
     * @param model
     * @return view name
     */
    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "form/items";
    }

    /**
     * 체크 박스 - 멀티
     * 등록 지역 : 서울, 부산, 제주
     * @ModelAttribute("regions")
     * 컨트롤러에 있는 별도의 메서드에 적용할 수 있다.
     * 해당 컨트롤러를 요청할 때 regions에서 반환한 값이 자동으로 모델(model)에 담기게 된다.
     * @return view name
     */
    @ModelAttribute("regions")
    public Map<String, String> regions() {
        Map<String, String> regions = new LinkedHashMap<>();
        regions.put("SEOUL", "서울");
        regions.put("BUSAN", "부산");
        regions.put("JEJU", "제주");
        return regions;
    }

    /**
     * 라디오 버튼 - 하나만 선택 가능
     * 상품 종류 : 도서, 식품, 기타
     * @return 해당 ENUM의 모든 정보를 배열로 반환
     */
    @ModelAttribute("itemTypes")
    public ItemType[] itemTypes() {
        return ItemType.values();
    }

    /**
     * 셀렉트 박스 - 하나만 선택 가능
     * 배송 방식: 빠른 배송, 일반 배송, 느린 배송
     * @ModelAttribute가 애노테치션이 특정 메소드가 붙어 있으면
     * 해당 컨트롤러 클래스의 모든 @RequestMapping 애노테이션이 붙은 메소드가 호출될 때마다
     * 그 메소드 호출 전에 @ModelAttribute가 붙은 메소드가 먼저 호출된다.
     * @return List<Delivery> 리스트 반환
     */
    @ModelAttribute("deliveryCodes")
    public List<DeliveryCode> deliveryCodes() {
        List<DeliveryCode> deliveryCodes = new ArrayList<>();
        deliveryCodes.add(new DeliveryCode("FAST", "빠른 배송"));
        deliveryCodes.add(new DeliveryCode("NORMAL", "일반 배송"));
        deliveryCodes.add(new DeliveryCode("SLOW", "느린 배송"));
        return deliveryCodes;
    }
}

