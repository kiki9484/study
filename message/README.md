## 메시지, 국제화

### 메시지
+ <b>상품명</b>이라는 단어를 모두 <b>상품이름으로</b> 바꿔야 된다고 해보자.
+ 여러 화면에 보이는 상품명, 가격, 수량 등 label에 있는 단어를 변경하려면 화면들을 다 찾아가면서 모두 변경해야 한다.
+ 화면 수가 많으면 많을수록 오류가 나기 쉽고 번거로워 진다.
+ 다양한 메시지를 한 곳에서 관리하도록 하는 기능을 <b>메시지 기능</b>이라 한다.
<br><br>

### 메시지 기능 예시
메시지 관리용 파일 생성: messages.properties

    label.item = 상품
    label.item.id = 상품 ID
    label.item.itemName = 상품명
    label.item.price = 가격
    label.item.quantity = 수량
    
    page.items = 상품 목록
    page.item = 상품 상세
    page.addItem = 상품 등록
    page.updateItem = 상품 수정

    button.save = 저장
    button.cancel = 취소

addForm.html에서 사용하기
    
    <label for="itemName" th:text="#{item.itemName}"></label>
    
editForm.html에서 사용하기

    <label for="itemName" th:text="#{item.itemName}"></label>

<br><br>

### 국제화
+ 메시지 파일(messages.properties)을 각 나라별로 관리하면 서비스를 국제화할 수 있다.
+ 언어별 파일을 따로 생성하여 관리하면 사이트를 국제화할 수 있다.
+ 어떤 나라에서 접근한 것이지 인식하는 방법은 HTTP accept-language 헤더 값을 사용하거나 사용자가 직접 언어를 선택하게 하고 쿠키를 사용해서 처리하면 된다.

<br><br>

### 국제화 기능 예시
영어 국제화 파일 생성: messages_en.properties

    label.item = Item
    label.item.id = Item ID
    label.item.itemName = Item Name
    label.item.price = price
    label.item.quantity = quantity
    
    page.items = Item List
    page.item = Item Detail
    page.addItem = Item Add
    page.updateItem = Item Update

    button.save = Save
    button.cancel = Cancel

한국 국제화 파일 생성: messages_ko.properties

    label.item = 상품
    label.item.id = 상품 ID
    label.item.itemName = 상품명
    label.item.price = 가격
    label.item.quantity = 수량
    
    page.items = 상품 목록
    page.item = 상품 상세
    page.addItem = 상품 등록
    page.updateItem = 상품 수정

    button.save = 저장
    button.cancel = 취소

<br><br>

### 스프링 부트 메시지 소스 설정
스프링 부트가 MessageSource를 자동으로 스프링 빈으로 등록한다.

<br><br>

### 스프링 부트 메시지 소스 기본 값
spring.messages.basename=messages
+ application.properties 파일에 있다.
+ MessageSource를 스프링 빈으로 등록하지 않고, 스프링 부트와 관련된 별도의 설정을 하지 않으면 messages라는 이름으로 기본 등록된다.
+ messages_en.properties, messages_ko.properties, messages.properties 파일만 등록하면 자동으로 인식된다.

<br><br>

### 메시지 파일 만들기
1. /resources/messages.properties : 기본 값으로 사용(한글)
2. /resources/messages_en.properties : 영어 국제화 사용

<br><br>

### 타임리프 메시지 적용
> 메시지 표현식: #{...}

페이징 이름에 적용
    
    <h2 th:text="#{page.addItem}">상품 등록</h2>

레이블에 적용

    <label for="itemName" th:text="#{label.item.itemName}">상품명</label>
    <label for="price" th:text="#{label.item.price}">가격</label>
    <label for="quantity" th:text="#{label.item.quantity}">수량</label>

버튼에 적용

    <button type="submit" th:text="#{button.save}">저장</button>
    <button type="button" th:text="#{button.cancel}">취소</button>