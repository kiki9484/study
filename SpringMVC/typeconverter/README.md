## 스프링 타입 컨버터

<br>

#### 스프링 타입 변환 적용 예시

+ 스프링 MVC 요청 파라미터
  + `@RequestParam`
  + `@ModelAttribute`
  + `@PathVariable`
+ `@Value` 등으로 YML 정보 읽기
+ XML에 넣은 스프링 빈 정보를 변환
+ 뷰를 렌더링 할 때 

<br>

### 타입 컨버터 - Converter

<br>

#### 컨버터 인터페이스
```java
package org.springframework.core.convert.converter;

public interface Converter<S, T> {
    T convert(S source);
}
```

<br><br>

### 컨버전 서비스 - ConversionService

<br>

#### ConversionService 인터페이스
```java
public interface ConversionService {
    boolean canConvert(@Nullable Class<?> sourceType, Class<?> targetType);
    boolean canConvert(@Nullable TypeDescriptor sourceType, TypeDescriptor targetType);
    
    <T> T convert(@Nullable Object source, Class<T> targetType);
    Object convert(@Nullable Object source, @Nullable TypeDescriptor sourceType, TypeDescriptor targetType);
}
```

<br>

#### 등록과 사용 분리
+ 컨버터를 등록할 때는 `StringToIntegerConverter` 같은 타입 컨버터를 명확하게 알아야 한다.
+ 반면에 컨버터를 사용하는 입장에서는 타입 컨버터를 전혀 몰라도 된다. 타입 컨버터들은 모두 컨버전 서비스 내부에 숨어서 제공된다.
+ 따라서 타입을 변환을 원하는 사용자는 컨버전 서비스 인터페이스에만 의존하면 된다. 물론 컨버전 서비스를 등록하는 부분과 사용하는 부분을 분리하고 의존관계 주입을 사용해야 한다.

<br><br>

### 스프링에 Converter 적용하기

#### WebConfig - 컨버터 등록
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToIntegerConverter());
        registry.addConverter(new IntegerToStringConverter());
        registry.addConverter(new StringToIpPortConverter());
        registry.addConverter(new IpPortToStringConverter());
    } 
}
```

<br>

#### 동작 과정
스프링은 내부에서 `ConversionService`를 제공한다.

우리는 `WebMvcConfigurer`가 제공하는 `addFormatters()`를 사용해서 추가하고 싶은 컨버터를 등록하면 된다.

이렇게 하면 스프링은 내부에서 사용하는 `ConversionService` 에 컨버터를 추가해준다.

<br>

#### 처리 과정
`@RequestParam`은 `@RequestParam`을 처리하는 `ArgumentResolver`인 `RequestParamMethodArgumentResolver`에서 `ConversionService`를 사용해서 타입을 변환한다. 

<br><br>

### 뷰 템플릿에 컨버터 적용하기

타임리프는 `${{...}}` 를 사용하면 자동으로 컨버전 서비스를 사용해서 변환된 결과를 출력해준다. 

스프링과 통합 되어서 스프링이 제공하는 컨버전 서비스를 사용하므로, 우리가 등록한 컨버터들을 사용할 수 있다.

<br>

**변수 표현식 :** `${...}`

**컨버전 서비스 적용 :** `${{...}}`

<br><br>

### 포맷터를 지원하는 컨버전 서비스

포맷터를 지원하는 컨버전 서비스를 사용하면 컨버전 서비스에 포맷터를 추가할 수 있다. 

내부에서 어댑터 패턴을 사용 해서 `Formatter`가 `Converter`처럼 동작하도록 지원한다.
+ `FormattingConversionService` 는 포맷터를 지원하는 컨버전 서비스이다.
+ `DefaultFormattingConversionService` 는 `FormattingConversionService` 에 기본적인 통화, 숫자 관 련 몇가지 기본 포맷터를 추가해서 제공한다.

<br>

#### DefaultFormattingConversionService 상속 관계
+ `FormattingConversionService` 는 `ConversionService` 관련 기능을 상속받기 때문에 결과적으로 컨버터도 포맷터도 모두 등록할 수 있다.
+ 사용할 때는 `ConversionService`가 제공하는 `convert`를 사용하면 된다. 
+ 추가로 스프링 부트는 `DefaultFormattingConversionService`를 상속 받은 `WebConversionService` 를
내부에서 사용한다.

<br>

#### Converter vs Formatter
+ `Converter` 는 범용(객체 객체)
+ `Formatter` 는 문자에 특화(객체 문자, 문자 객체) + 현지화(Locale) 
  + `Converter` 의 특별한 버전

<br>

#### 포맷터 적용하기

포맷터를 웹 애플리케이션에 적용해보자. 

**WebConfig - 수정**

```java
@Configuration
public class WebConfig implements WebMvcConfigurer { 
    @Override 
    public void addFormatters(FormatterRegistry registry) {
    //주석처리 우선순위
    //registry.addConverter(new StringToIntegerConverter()); 
    // registry.addConverter(new IntegerToStringConverter()); 
    // registry.addConverter(new StringToIpPortConverter()); 
    // registry.addConverter(new IpPortToStringConverter());

    //추가
    registry.addFormatter(new MyNumberFormatter());
    }
}
```

<br>

#### 주의
+ `StringToIntegerConverter`, `IntegerToStringConverter`를 주석처리 하자. 
+ `MyNumberFormatter`도 숫자 문자, 문자 숫자로 변경하기 때문에 둘의 기능이 겹친다. 
+ 우선순위는 컨버터가 우선하므로 포맷터가 적용되지 않고, 컨버터가 적용된다.


<br><br>

### 스프링이 제공하는 기본 포맷터

스프링은 자바에서 기본으로 제공하는 타입들에 대해 수 많은 포맷터를 기본으로 제공한다.

스프링은 애노테이션 기반으로 원하는 형식을 지정해서 사용할 수 있는 매우 유용한 포맷터 두 가지를 기본으로 제공한다.

`@NumberFormat` : 숫자 관련 형식 지정 포맷터 사용, `NumberFormatAnnotationFormatterFactory` 

`@DateTimeFormat` : 날짜 관련 형식 지정 포맷터 사용, `Jsr310DateTimeFormatAnnotationFormatterFactory`

<br>

### 스프링이 제공하는 기본 포맷터 - 예시

**FormatterController**
```java
@Controller
public class FormatterController {

    @GetMapping("/formatter/edit")
    public String formatterForm(Model model) {
        Form form = new Form();
        form.setNumber(10000);
        form.setLocalDateTime(LocalDateTime.now());
        model.addAttribute("form", form);
        return "formatter-form";
    }
    
    @PostMapping("/formatter/edit")
    public String formatterEdit(@ModelAttribute Form form) {
        return "formatter-view";
    }
    
    @Data
    static class Form {
        @NumberFormat(pattern = "###,###")
        private Integer number;
 
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime localDateTime;
    }
}
```

<br>

#### 컨버전 서비스 사용 시 주의!
+ 메시지 컨버터(`HttpMessageConverter`)에는 컨버전 서비스가 적용되지 않는다.
+ `HttpMessageConverter`의 역할은 HTTP 메시지 바디의 내용을 객체로 변환하거나 객체를 HTTP 메시지 바디에 입력하는 것이다.
+ JSON 결과로 만들어지는 숫자나 날짜 포맷을 변경하고 싶으면 해당 라이브러리가 제공하는 설정을 통해서 포맷을 지정해야 한다.
