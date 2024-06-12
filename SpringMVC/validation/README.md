## 검증
웹 서비스는 폼 입력시 오류가 발생하면, 고객이 입력한 <b>데이터를 유지</b>한 상태로 어떤 오류가 발생했는지 알려주어야 한다.

컨트롤러의 중요한 역할중 하나는 HTTP 요청이 정상인지 검증하는 것이다.

<br>

#### 상품 저장 성공
<img src="/Users/park-uigyun/Documents/GitHub/SpringMVC/validation/images/Item_success.png" width="370px" height="300px"></img><br/>

사용자가 상품 등록 폼에서 정상 범위의 데이터를 입력하면, 서버에서는 검증 로직이 통과하고, 상품을 저장하고, 상품 상세 화면으로 redirect한다.

<br>

#### 상품 저장 실패
<img src="/Users/park-uigyun/Documents/GitHub/SpringMVC/validation/images/Item_fail.png" width="370px" height="300px"></img><br/>

고객이 상품 등록 폼에서 상품명을 입력하지 않거나 검증 범위를 넘어서면 서버 검증 로직이 실패해야 한다. 

검증에 실패한 경우 고객에게 다시 상품 등록 폼을 보여주고, 어떤 값을 잘못 입력했는지 알려주어야 한다.

<br><br>

### BindingResult
+ 스프링이 제공하는 검증 오류를 보관하는 객체이다.
+ 검증 오류가 발생하면 여기에 보관하면 된다. `BindingResult` 가 있으면 `@ModelAttribute` 에 데이터 바인딩 시 오류가 발생해도 컨트롤러가 호출된다!
+ `BindingResult` 는 Model에 자동으로 포함된다.

<br>

#### @ModelAttribute에 바인딩 시 타입 오류가 발생
+ `BindingResult`가 없음:  400 오류가 발생하면서 컨트롤러가 호출되지 않고, 오류 페이지로 이동한다. 
+ `BindingResult`가 있음: 오류 정보(`FieldError`)를 `BindingResult` 에 담아서 컨트롤러를 정상 호출한다.

<br>

#### @BindingResult 위치
+ `BindingResult`는 검증할 대상 바로 다음에 와야 한다.
+ 예를 들어서 `@ModelAttribute Item item` 바로 다음에 `BindingResult`가 와야 한다.

<br>

#### BindingResult와 Errors
+ `org.springframework.validation.Errors` `org.springframework.validation.BindingResult`
`BindingResult` 는 인터페이스이고, `Errors` 인터페이스를 상속받고 있다.
+ 실제 넘어오는 구현체는 `BeanPropertyBindingResult`라는 것인데, 둘다 구현하고 있으므로 `BindingResult` 대신에 `Errors` 를 사용해도 된다.
+ `Errors` 인터페이스는 단순한 오류 저장과 조회 기능을 제공한다. `BindingResult`는 여기에 더해서 추가적인 기능들을 제공한다.
+ `addError()`도 `BindingResult`가 제공하므로 여기서는 `BindingResult`를 사용하자.
+ 주로 관례상 `BindingResult` 를 많이 사용한다.

<br><br>

### 필드 오류 - FieldError
오류가 발생한 경우 사용자 입력 값을 보관하는 별도의 방법이 필요하다. 그리고 이렇게 보관한 사용자 입력 값을 검증 오류 발생시 화면에 다시 출력해야 한다.

타입 오류로 바인딩에 실패하면 스프링은 `FieldError`를 생성하면서 사용자가 입력한 값을 넣어둔다. 그리고 해당 오류를 `BindingResult` 에 담아서 컨트롤러를 호출한다. 따라서 타입 오류 같은 바인딩 실패시에도 사용자의 오류 메시지를 정상 출력할 수 있다.

`FieldError`는 오류 발생시 사용자 입력 값을 저장하는 `rejectedValue`매개변수를 제공한다.

`bindingFailure`는 타입 오류 같은 바인딩이 실패했는지 여부를 적어주면 된다.

<br>

#### 타임리프의 사용자 입력 값 유지
> th:field="*{필드이름}"

+ 정상 상황 : 모델 객체의 값을 사용한다.
+ 오류 발생 : `FieldError`에서 보관한 값을 사용해서 값을 출력한다.

<br>

#### 스프링의 바인딩 오류 처리

```java
public FieldError(String objectName, String field, String defaultMessage) {}
public FieldError(String objectName, String field, @Nullable Object rejectedValue, boolean bindingFailure, @Nullable String[] codes, @Nullable Object[] arguments, @Nullable String defaultMessage)
```

+ `objectName` : `@ModelAttribute` 이름
+ `field` : 오류가 발생한 필드 이름
+ `rejectedValue` : 사용자가 입력한 값(거절된 값)
+ `bindingFailure` : 타입 오류 같은 바인딩 실패인지, 검증 실패인지 구분 값 
+ `codes` : 메시지 코드
+ `arguments` : 메시지에서 사용하는 인자
+ `defaultMessage` : 기본 오류 메시지

<br><br>

### 글로벌 오류 - ObjectError
특정 필드를 넘어서는 오류가 있으면 `ObjectError` 객체를 생성해서 `bindingResult` 에 담아두면 된다.

```java
public ObjectError(String objectName, String defaultMessage) {}
public ObjectError(String objectName, @Nullable String[] codes, @Nullable Object[] arguments, @Nullable String defaultMessage)
```

+ `objectName` : `@ModelAttribute` 의 이름
+ `codes` : 메시지 코드
+ `arguments` : 메시지에서 사용하는 인자
+ `defaultMessage` : 오류 기본 메시지

<br><br>

### 오류 코드와 메시지 처리
`FieldError`, `ObjectError`의 생성자는 `codes`, `arguments`를 제공한다. 이것은 오류 발생시 오류 코드로 메시지를 찾기 위해 사용된다.

<br>

#### errors 메시지 파일 생성
`errors.properties` 라는 별도의 파일로 관리해보자.

1. 스프링 부트가 해당 메시지 파일을 인식할 수 있게 다음 설정을 추가한다.
2. `messages.properties`, `errors.properties` 두 파일을 모두 인식한다.(생략하면 `messages.properties` 를 기본으로 인식한다.)

<br>

#### 스프링 부트 메시지 설정 추가

`application.properties`
```text
spring.messages.basename=messages,errors
```

<br>

#### errors.properties 추가
`src/main/resources/errors.properties` 
```text
required.item.itemName=상품 이름은 필수입니다.
range.item.price=가격은 {0} ~ {1} 까지 허용합니다.
max.item.quantity=수량은 최대 {0} 까지 허용합니다.
totalPriceMin=가격 * 수량의 합은 {0}원 이상이어야 합니다. 현재 값 = {1}
```

<br>

#### 사용 예시
```java
new FieldError("item", "price", item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 1000000}
```

<br><br>

### 오류 코드와 메시지 처리 간편화
컨트롤러에서 `BindingResult`는 검증해야 할 객체인 `target` 바로 다음에 온다.

따라서 `BindingResult` 는 이미 검증해야 할 객체인 `target` 을 알고 있다.

<br>

#### rejectValue() 

검증 대상을 알고 있기 때문에 field만 써도 된다. 

```java
void rejectValue(@Nullable String field, String errorCode, @Nullable Object[] errorArgs, @Nullable String defaultMessage);
```

+ `field` : 오류 필드명
+ `errorCode` : messageResolver를 위한 오류 코드
+ `errorArgs` : 오류 메시지에서 인자를 치환하기 위한 값
+ `defaultMessage` : 오류 메시지를 찾을 수 없을 때 사용하는 기본 메시지

<br>

#### reject()
```java
void reject(String errorCode, @Nullable Object[] errorArgs, @Nullable String defaultMessage);
```

+ `errorCode` : messageResolver를 위한 오류 코드
+ `errorArgs` : 오류 메시지에서 인자를 치환하기 위한 값
+ `defaultMessage` : 오류 메시지를 찾을 수 없을 때 사용하는 기본 메시지

<br>

#### MessageCodesResolver
+ 검증 오류 코드로 메시지 코드들을 생성한다.
+ `MessageCodesResolver` 인터페이스이고, `DefaultMessageCodesResolver`는 기본 구현체이다. 
+ 주로 `ObjectError`, `FieldError`와 함께 사용한다.

<br>

#### DefaultMessageCodesResolver의 기본 메시지 생성 규칙 - 객체 오류 

```
객체 오류의 경우 다음 순서로 2가지 생성 
1.: code + "." + object name 
2.: code

예) 오류 코드: required, object name: item
1.: required.item
2.: required
```

<br>

#### DefaultMessageCodesResolver의 기본 메시지 생성 규칙 - 필드 오류

```
필드 오류의 경우 다음 순서로4가지 메시지 코드 생성 
1.: code + "." + object name + "." + field 
2.: code + "." + field
3.: code + "." + field type
4.: code

예) 오류 코드: typeMismatch, object name "user", field "age", field type: int 
1. "typeMismatch.user.age"
2. "typeMismatch.age"
3. "typeMismatch.int"
```

<br>

#### 동작 방식
1. `rejectValue()`, `reject()`는 내부에서 `MessageCodesResolver`를 사용한다. 여기에서 메시지 코드들을 생성한다.
2. `FieldError`, `ObjectError` 의 생성자를 보면, 오류 코드를 하나가 아니라 여러 오류 코드를 가질 수 있다. `MessageCodesResolver`를 통해서 생성된 순서대로 오류 코드를 보관한다.
3. `codes [range.item.price, range.price, range.java.lang.Integer, range]`
4. FieldError `rejectValue("itemName", "required")`는 4가지 오류 코드를 생성한다.
   1. `required.item.itemName`
   2. `required.itemName`
   3. `required.java.lang.String`
   4. `required`
5. ObjectError `reject("totalPriceMin")`는 2가지 오류 코드를 생성한다.
   1. `totalPriceMin.item`
   2. `totalPriceMin`

<br>

#### 오류 메시지 출력
+ 타임리프 화면을 렌더링 할 때 `th:errors` 가 실행된다. 
+ 만약 이때 오류가 있다면 생성된 오류 메시지 코드를 순서대로 돌아가면서 메시지를 찾는다. 그리고 없으면 디폴트 메시지를 출력한다.

****

<br><br>

## Bean Vailidation
검증 로직을 모든 프로젝트에 적용할 수 있게 공통화하고, 표준화 한 것이 Bean Validation 이다. 

Bean Validation을 잘 활용하면, 애노테이션 하나로 검증 로직을 매우 편리하게 적용할 수 있다.

<br>

### Bean Validation 이란?
Bean Validation은 특정한 구현체가 아니라 Bean Validation 2.0(JSR-380)이라는 기술 표준이다. 

쉽게 이야기해서 검증 애노테이션과 여러 인터페이스의 모음이다. 

Bean Validation을 구현한 기술 중 일반적으로 사용하는 구현체는 하이버네이트 Validator이다. 

<br>

### Bean Validation 의존관계 추가
`build.gradle` 
```
implementation 'org.springframework.boot:spring-boot-starter-validation'
```
`spring-boot-starter-validation` 의존관계를 추가하면 라이브러리가 추가 된다.

`Jakarta Bean Validation`
  + `jakarta.validation-api` : Bean Validation 인터페이스
  + `hibernate-validator` 구현체 

<br>

### Bean Validation 사용 예시
```java
@Data
public class Item {
    private Long id;
    
    @NotBlank
    private String itemName;
    
    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;
    
    @NotNull
    @Max(9999)
    private Integer quantity;
    
    public Item() {}
    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
```
+ `@NotBlank` : 빈값 + 공백만 있는 경우를 허용하지 않는다.
+ `@NotNull` : `null`을 허용하지 않는다.
+ `@Range(min = 1000, max = 1000000)` : 범위 안의 값이어야 한다.
+ `@Max(9999)` : 최대 9999까지만 허용한다.

<br><br>

### 스프링 MVC가 Bean Validator를 사용하는 법
스프링 부트가 `spring-boot-starter-validation` 라이브러리를 넣으면 자동으로 Bean Validator를 인지하고 스프링에 통합한다.

스프링 부트는 자동으로 글로벌 Validator로 등록한다.
+ `LocalValidatorFactoryBean` 을 글로벌 Validator로 등록한다. 
+ 이 Validator는 `@NotNull`같은 애노테이션을 보고 검증을 수행한다. 
+ 이렇게 글로벌 Validator가 적용되어 있기 때문에, `@Valid`, `@Validated`만 적용하면 된다.
+ 검증 오류가 발생하면 `FieldError`, `ObjectError`를 생성해서 `BindingResult`에 담아준다.
+ 검증시 `@Validated`, `javax.validation.@Valid`를 사용하려면 `build.gradle` 의존관계 추가가 필요하다.
  + `implementation 'org.springframework.boot:spring-boot-starter-validation'`
+ `@Validated`는 스프링 전용 검증 애노테이션이고, `@Valid` 는 자바 표준 검증 애노테이션이다. 
  + `@Validated`는 내부에 `groups` 라는 기능을 포함하고 있다. 

<br>

### 검증 순서
1. `@ModelAttribute` 각각의 필드에 타입 변환 시도 
   1. 성공하면 다음으로
   2. 실패하면 `typeMismatch` 로 `FieldError` 추가 
2. Validator 적용

BeanValidator는 바인딩에 실패한 필드는 BeanValidation을 적용하지 않는다.

타입 변환에 성공해서 바인딩에 성공한 필드여야 BeanValidation 적용이 의미 있다.

`@ModelAttribute` -> 각각의 필드타입 변환 시도 -> 변환에 성공한 필드만 BeanValidation 적용

<br><br>

### Bean Validation - 에러 코드

오류 코드가 애노테이션 이름으로 등록된다.(마치`typeMismatch` 와 유사하다)

`NotBlank`라는 오류 코드를 기반으로 `MessageCodesResolver`를 통해 다양한 메시지 코드가 순서대로 생성된다.

+ @NotBlank
  + NotBlank.item.itemName 
  + NotBlank.itemName 
  + NotBlank.java.lang.String 
  + NotBlank
+ @Range
  + Range.item.price
  + Range.price
  + Range.java.lang.Integer
  + Range

<br><br>

### 메시지 등록

`{0}` 은 필드명이고, `{1}`, `{2}` ... 은 각 애노테이션 마다 다르다.

`errors.properties` 
```
#Bean Validation 추가 
NotBlank={0} 공백X
Range={0}, {2} ~ {1} 허용 
Max={0}, 최대 {1}
```

<br>

#### BeanValidation 메시지 찾는 순서
1. 생성된 메시지 코드 순서대로 `messageSource` 에서 메시지 찾기
2. 애노테이션의 `message` 속성 사용 `@NotBlank(message = "공백! {0}")`
3. 라이브러리가 제공하는 기본 값 사용 공백일 수 없습니다.

<br><br>

### 오브젝트 오류 다루기
오브젝트 오류(글로벌 오류)의 경우 `@ScriptAssert`을 사용하는 것 보다는 오브젝트 오류 관련 부분만 직접 자바 코드로 작성하는 것을 권장한다.

<br><br>

### Form 전송 객체 분리

<br>

#### 폼 데이터 전달에 Item 도메인 객체 사용
`HTML Form -> Item -> Controller -> Item -> Repository`

<b>장점</b> : Item 도메인 객체를 컨트롤러, 리포지토리 까지 직접 전달해서 중간에 Item을 만드는 과정이 없어서 간단하다.

<b>단점</b> : 간단한 경우에만 적용할 수 있다. 수정시 검증이 중복될 수 있고, groups를 사용해야 한다.

<br>

#### 폼 데이터 전달을 위한 별도의 객체 사용
`HTML Form -> ItemSaveForm -> Controller -> Item 생성 -> Repository`

<b>장점</b> : 전송하는 폼 데이터가 복잡해도 거기에 맞춘 별도의 폼 객체를 사용해서 데이터를 전달 받을 수 있다. 보통 등록과, 수정용으로 별도의 폼 객체를 만들기 때문에 검증이 중복되지 않는다.

<b>단점</b>: 폼 데이터를 기반으로 컨트롤러에서 Item 객체를 생성하는 변환 과정이 추가된다.

<br>

#### 등록과 수정에 별도의 객체를 사용해야 하는 이유

등록과 수정은 완전히 다른 데이터가 넘어온다. 

예를 들면 등록 시에는 로그인id, 주민번호 등등을 받을 수 있지만, 수정시에는 이런 부분이 빠진다. 그리고 검증 로직도 많이 달라진다. 

그래서 `ItemUpdateForm`이라는 별도의 객체로 데이터를 전달받는 것이 좋다.

<br><br>

### Bean Validation - HTTP 메시지 컨버터

`@Valid`, `@Validated`는 `HttpMessageConverter`(`@RequestBody`)에도 적용할 수 있다.

<br>

#### API의 경우 3가지 경우를 나누어 생각해야 한다.
+ 성공 요청: 성공
+ 실패 요청: JSON을 객체로 생성하는 것 자체를 실패한다.
  + `HttpMessageConverter`에서 요청 JSON을 `ItemSaveForm`객체로 생성하는데 실패한다.
  + 이 경우 `ItemSaveForm` 객체를 만들지 못하기 때문에 컨트롤러 자체가 호출되지 않고 그 전에 예외가 발생한다. 
+ 검증 오류 요청: JSON을 객체로 생성하는 것은 성공했고, 검증에서 실패함
  +  필요한 데이터만 뽑아서 별도의 API 스펙을 정의하고 그에 맞는 객체를 만들어서 반환해야 한다.

****

<br><br>

## 참고

### @ModelAttribute vs @RequestBody


#### @ModelAttribute
+ HTTP 요청 파리미터를 처리하는 `@ModelAttribute`는 각각의 필드 단위로 세밀하게 적용된다.
+ 그래서 특정 필드 에 타입이 맞지 않는 오류가 발생해도 나머지 필드는 정상 처리할 수 있었다.
+ 특정 필드가 바인딩 되지 않아도 나머지 필드는 정상 바인딩 되고, Validator를 사용한 검증도 적용할 수 있다.


#### @RequestBody
+ `HttpMessageConverter`는 `@ModelAttribute`와 다르게 각각의 필드 단위로 적용되는 것이 아니라, 전체 객체 단위로 적용된다.
+ 따라서 메시지 컨버터의 작동이 성공해서 `ItemSaveForm` 객체를 만들어야 `@Valid`, `@Validated` 가 적용된다. 
+ `HttpMessageConverter` 단계에서 실패하면 예외가 발생한다. 예외 발생시 원하는 모양으로 예외를 처리하는 방법
  은 예외 처리 부분에서 다룬다.

<br><br>  

### 타임리프 스프링 검증 오류 통합 기능

타임리프는 스프링의 `BindingResult` 를 활용해서 편리하게 검증 오류를 표현하는 기능을 제공한다.

`#fields` : `#fields` 로 `BindingResult` 가 제공하는 검증 오류에 접근할 수 있다.

`th:errors` : 해당 필드에 오류가 있는 경우에 태그를 출력한다.(`th:if` 의 편의 버전)

`th:errorclass` : `th:field` 에서 지정한 필드에 오류가 있으면 `class` 정보를 추가한다.

<br><br>

### 검증과 오류 메시지 공식 메뉴얼
https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html#validation-and-error-messages

<br><br>

### 하이버네이트 Validator 관련 링크

공식 사이트: http://hibernate.org/validator/

공식 메뉴얼: https://docs.jboss.org/hibernate/validator/6.2/reference/en-US/html_single/ 

검증 애노테이션 모음: https://docs.jboss.org/hibernate/validator/6.2/reference/en-US/html_single/#validator-defineconstraints-spec
