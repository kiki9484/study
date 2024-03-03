## 예외 처리와 오류 페이지

### Exception

#### 자바 직접 실행

자바의 메인 메서드를 직접 실행하는 경우 `main`이라는 이름의 쓰레드가 실행된다.

실행 도중에 예외를 잡지 못하고 처음 실행한 `main()` 메서드를 넘어서 예외가 던져지면, 예외 정보를 남기고 해당 쓰레드는 종료된다.

<br>

#### 웹 애플리케이션
웹 애플리케이션은 사용자 요청별로 별도의 쓰레드가 할당되고, 서블릿 컨테이너 안에서 실행된다.

애플리케이션에서 발생한 예외를 try ~ catch로 예외를 잡아서 처리하면 아무런 문제가 없다.

애플리케이션에서 예외를 잡지 못하고, 서블릿 밖으로까지 예외가 전달되면 아래와 같이 동작한다.

> WAS(여기까지 전파) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(예외발생)

<br>

### 서블릿 예외 처리 - 오류 페이지 작동 원리 

`예외 발생과 오류 페이지 요청 흐름`
> 1. WAS(여기까지 전파) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(예외발생)
> 2. WAS `/error-page/500` 다시 요청 -> 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러(/error-page/500) -> View

+ 예외가 발생해서 WAS까지 전파된다.
+ WAS는 오류 페이지 경로를 찾아서 내부에서 오류 페이지를 호출한다. 이 때 오류 페이지 경로로 필터, 서블릿, 인터셉터, 컨트롤러가 모두 다시 호출된다.

<br>

### 서블릿 예외 처리 - 필터 

오류가 발생하면 오류 페이지를 출력하기 위해 WAS 내부에서 다시 한 번 호출이 일어난다. (필터, 서블릿, 인터셉터도 모두 다시 호출된다)

서버 내부에서 오류 페이지를 호출한다고 해서 해당 필터나 인터셉터를 한 번 더 호출하는 것은 <b>비효율적</b>이다.

서블릿은 DispatcherType 이라는 추가 정보를 제공한다.

<br>

#### DispatcherType

서블릿은 실제 고객이 요청한 것인지, 서버가 내부에서 오류 페이지를 요청하는 것인지 DispatcherType으로 구분할 수 있다.

`javax.servlet.DispatcherType`

```java
public enum DispatcherType{
    REQUEST, // 클라이언트 요청
    ERROR, // 오류 요청
    FORWARD, // 서블릿에서 다른 서블릿이나 JSP를 호출할 때
    INCLUDE, // 서블릿에서 다른 서블릿이나 JSP의 결과를 포함할 때
    ASYNC // 서블릿 비동기 호출
}
```

<br><br>

### 서블릿 예외 처리 - 인터셉터

인터셉터는 요청 경로에 따라서 추가하거나 제외할 수 있다.

오류 페이지 경로를 excludePathPatterns를 사용해서 빼내줄 수 있다.

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "*.ico", "/error", "/error-page/**"); //오류 페이지 경로
    }
}
```

<br><br>

### 전체 흐름 정리

`/hello` 정상 요청
> WAS(/hello, dispatchType=REQUEST) -> 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러 -> View

<br>

`error-ex` 오류 요청

+ 필터는 DispatchType으로 중복 제거(dispatchType=REQUEST)
+ 인터셉터는 경로 정보로 중복 호출 제거(excludePathPatterns("/error-page/**"))
> 1. WAS(/error-ex, dispatchType=REQUEST) -> 필터 -> 서블릿 -> 인터셉터 -> 컨트롤러
> 2. WAS(여기까지 전파) <- 필터 <- 서블릿 <- 인터셉터 <- 컨트롤러(예외발생)
> 3. WAS 오류 페이지 확인
> 4. WAS(/error-page/500, dispatchType=ERROR) -> 필터(x) -> 서블릿 -> 인터셉터(x) -> 컨트롤러(/error-page/500) -> View

****

<br><br>

## 스프링 부트 오류 페이지 설정

<br>

### 스프링 부트를 사용하지 않고 예외 처리 페이지 만드는 과정
1. WebSererCustomizer를 만든다.
2. 예외 종류에 따라서 ErrorPage를 추가한다.
3. 예외 처리용 컨트롤러 ErrorPageController를 만든다.


<br>

### 스프링 부트가 기본으로 에러 페이지 설정을 제공해준다.

`ErrorPage`를 자동으로 등록한다. `/error`라는 경로로 기본 오류 페이지를 설정한다.
  + new ErrorPage("/error") : 상태코드와 예외를 설정하지 않으면 기본 오류 페이지로 사용된다.
  + 서블릿 밖으로 예외가 발생하거나 response.sendError(...)가 호출되면 모든 오류는 `/error`를 호출한다.

<br>

`BasicErrorController`라는 스프링 컨트롤러를 자동 등록한다.
  + `ErrorPage`에서 등록한 `/error`를 매핑해서 처리하는 컨트롤러다.

<br>

오류가 발생했을 때 오류 페이지로 `/error`를 기본 요청한다, 스프링 부트가 자동 등록한 `BasicErrorController`는 이 경로를 기본으로 받는다.

<br>

`ErrorMvcAutoConfiguration` 클래스가 오류 페이지를 자동으로 등록하는 역할을 한다.

<br><br>

### BasicErrorController
`BasicErrorController`는 기본적인 로직이 모두 개발되어 있다.

개발자는 오류 페이지 화면만 `BasicErrorController`가 제공하는 규칙과 우선순위에 따라서 등록하면 된다.

<br>

#### 뷰 선택 우선순위
1. 뷰 템플릿
   + resources/templates/error/500.html
   + resources/templates/error/5xx.html
2. 정적 리소스(static, public)
   + resources/static/error/400.html
   + resources/static/error/404.html
   + resources/static/error/4xx.html
3. 적용 대상이 없을 때 뷰 이름(error)
   + resources/templates/error.html

참고) 예외는 500으로 처리한다.

<br><br>

### BasicErrorController가 제공하는 기본 정보들

`BasicErrorController` 컨트롤러는 다음 정보를 model에 담아서 뷰에 전달한다.

뷰 템플릿은 이 값을 활용해서 출력할 수 있다.

```
timestamp: Fri Feb 05 00:00:00 KST 2021
status: 400
error: Bad Request
exception: org.springframework.validation.BindException * trace: 예외 trace
message: Validation failed for object='data'. Error count: 1
errors: Errors(BindingResult)
path: 클라이언트 요청 경로 (/hello)
```

<br>

`BasicErrorController` 오류 컨트롤러에서 오류 정보를 model에 포함할지 여부를 선택할 수 있다.

`application.properties`
```
server.error.include-exception=false : exception 포함 여부 (true, false)
server.error.include-message=never : message 포함 여부 (never, always, on_param)
server.error.include-stacktrace=never : trace 포함 여부 (never, always, on_param) 
server.error.include-binding-errors=never : errors 포함 여부 (never, always, on_param)
```
+ `on_param` 으로 설정하고 다음과 같이 HTTP 요청시 파라미터를 전달하면 해당 정보들이 `model` 에 담겨서 뷰 템플릿에서 출력된다.
+ 운영 서버에서는 절대로 이 정보를 노출해서는 안 된다.
+ 사용자에게는 간단한 오류 메시지를 보여주고 오류는 서버에 로그를 남겨서 로그로 확인해야 한다. 

<br>

#### 스프링 부트 오류 관련 옵션

`application.properties`
```
server.error.whitelabel.enabled=true : 오류 처리 화면을 못 찾을 시, 스프링 whitelabel 오류 페이지 적용
server.error.path=/error : 오류 페이지 경로, 스프링이 자동 등록하는 서블릿 글로벌 오류 페이지 경로와 `BasicErrorController` 오류 컨트롤러 경로에 함께 사용된다.
```
