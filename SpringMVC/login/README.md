## 로그인 처리 - Cookie, Session

<br>

### 도메인

도메인이란 화면, UI, 기술 인프라 등등의 영역을 제외한 시스템이 구현해야 하는 핵심 비즈니스 업무 영역을 말한다.

web, service, repository 계층은 domain을 알고 있다.(domain에 의존한다)

domain은 web, service, repository 계층에 대해서 몰라야 된다.(domain은 나머지 계층을 참조해서는 안된다)

<br>

### Cookie

서버에서 로그인에 성공하면 HTTP 응답에 쿠키를 담아서 브라우저에 전달하면 브라우저는 앞으로 해당 쿠키를 지속해서 보내준다.

<br>

#### 쿠키에는 영속 쿠키와 세션 쿠키가 있다.
+ 영속 쿠키: 만료 날짜를 입력하면 해당 날짜까지 유지
+ 세션 쿠키: 만료 날짜를 생략하면 브라우저 종료시 까지만 유지

<br>

#### 쿠키 생성 로직
+ 로그인에 성공하면 쿠키를 생성하고 `HttpServletResponse`에 담는다.
+ 쿠키 이름은 `memberId`이고, 값은 회원의 `id`를 담아둔다.
+ 웹 브라우저는 종료 전까지 회원의 `id` 를 서버에 계속 보내줄 것이다.

```java
Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.class))
response.addCookie(idCookie);
```

<br><br>

### 쿠키와 보안 문제
쿠키 값은 임의로 변경할 수 있다.
  + 클라이언트가 쿠키를 강제로 변경하면 다른 사용자가 된다.

쿠키에 보관된 장소는 훔쳐갈 수 있다.

쿠기를 한 번 훔쳐가면 평생 사용할 수 있다.

<br><br>

### Session
위의 문제를 해결하려면 결국 중요한 정보를 모두 서버에 저장해야 한다. 

그리고 클라이언트와 서버는 추정 불가능한 임의의 식별자 값으로 연결해야 한다.

<br><br>

### 세션 동작 방식

![세션_로그인.png](src%2Fmain%2Fresources%2Fimages%2F%EC%84%B8%EC%85%98_%EB%A1%9C%EA%B7%B8%EC%9D%B8.png)
+ 사용자가 loginId, password를 보내면 서버에서 해당 사용자가 맞는지 확인한다.

<br>

![세션_세션관리.png](src%2Fmain%2Fresources%2Fimages%2F%EC%84%B8%EC%85%98_%EC%84%B8%EC%85%98%EA%B4%80%EB%A6%AC.png)
+ 서버에서 세션 id를 생성하는데, 추정 불가능한 값이어야 한다.
+ UUID를 사용해 추정 불가능한 값을 만든다.
+ 생성된 세션 ID와 세션에 보관할 값(memberA)을 서버의 세션 저장소에 보관한다.

<br>

![세션_세션id를-쿠키로-전달.png](src%2Fmain%2Fresources%2Fimages%2F%EC%84%B8%EC%85%98_%EC%84%B8%EC%85%98id%EB%A5%BC-%EC%BF%A0%ED%82%A4%EB%A1%9C-%EC%A0%84%EB%8B%AC.png)
+ 클라이언트와 서버는 쿠키로 연결된다.
+ 서버는 클라이언트에 mySessionId라는 이름으로 세션ID만 쿠키에 담아서 전달한다.
+ 클라이언트는 쿠키 저장소에 mySessionId 쿠키를 보관한다.
+ 오직 추정 불가능한 세션 ID만 쿠키를 통해 클라이언트로 전달한다.

<br>
 
![세션_로그인-이후-접근.png](src%2Fmain%2Fresources%2Fimages%2F%EC%84%B8%EC%85%98_%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EC%9D%B4%ED%9B%84-%EC%A0%91%EA%B7%BC.png)
+ 클라이언트는 요청 시 항상 mySessionId 쿠키를 전달한다.
+ 서버에서는 클라이언트가 전달한 mySessionID 쿠키 정보를 세션 저장소를 조회해서 로그인 시 보관한 세션 정보를 사용한다.

<br><br>

### Servlet HttpSession 사용
서블릿을 통해 `HttpSession`을 생성하면 다음과 같은 쿠키를 생성한다. 

쿠키 이름이 `JSESSIONID` 이고, 값은 추정 불가능한 랜덤 값이다.

<br>


#### 세션 생성: `request.getSession(true)` 사용
```java
public HttpSession getSession(boolean create);
```
  
<br>

#### 세션의 `create` 옵션
+ `request.getSession(true)`
  + 세션이 있으면 기존 세션을 반환한다.
  + 세션이 없으면 새로운 세션을 생성해서 반환한다. 
+ `request.getSession(false)`
  + 세션이 있으면 기존 세션을 반환한다.
  + 세션이 없으면 새로운 세션을 생성하지 않는다. `null` 을 반환한다.

<br>

#### 세션에 로그인 회원 정보 보관
```java
session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
```

<br>

#### 세션 제거
```java
session.invalidate();
```

<br><br>

### @SessionAttribute
스프링이 세션을 더 편리하게 사용할 수 있도록 @SessionAttribute를 지원한다.

<br>

#### 이미 로그인 된 사용자 찾기
```java
@SessionAttribute(name = "loginMember", required = false) Member loginMember`
```
+ 세션을 새로 생성하지는 않는다.
+ 세션에 회원 데이터가 없으면 null을 반환한다.

****

<br><br>

## 로그인 처리 - 인터셉터
애플리케이션의 여러 로직에서 공통으로 관심이 있는 것을 <b>공통 관심사</b>라고 한다.

웹과 관련된 공통 관심사는 <b>스프링 인터셉터</b>를 사용하는 것이 좋다.

<br>

### 스프링 인터셉터

`스프링 인터셉터 흐름`

> HTTP 요청 -> WAS -> (서블릿)필터 -> (디스패처)서블릿 -> 스프링 인터셉터 -> 컨트롤러`
+ 스프링 인터셉터는 디스패처 서블릿과 컨트롤러 사이에서 컨트롤러 호출 직전에 호출 된다.
+ 스프링 인터셉터는 스프링 MVC가 제공하는 기능이기 때문에 디스패처 서블릿 이후에 등장하게 된다.
  + 스프링 MVC의 시작점이 디스패처 서블릿이라고 볼 수 있다.
+ 스프링 인터셉터는 URL패턴을 적용할 수 있는데 매우 정밀하게 설정 가능하다.

<br>

`스프링 인터셉터 제한`
> HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 스프링 인터셉터 -> 컨트롤러 

> HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 스프링 인터셉터(적적하지 않은 요청일 경우 컨트롤러 호출 X)


<br>

`스프링 인터셉터 체인`
> HTTP 요청 -> WAS -> 필터 -> 서블릿 -> 인터셉터1 -> 인터셉터2 -> 컨트롤러

<br>

### 스프링 인터셉터 인터페이스

스프링의 인터셉터를 사용하려면 `HandlerInterceptor` 인터페이스를 구현하면 된다.

```java
public interface HandlerInterceptor{ 
    default boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {}
    default void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {}
    default void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {}
}
```

+ 인터셉터는 컨트롤러 호출 전(preHandle), 호출 후(postHandle), 요청 완료 이후(afterCompletion)을 제공한다.
+ 인터셉터는 어떤 컨트롤러(handler)가 호출되었는지 호출 정보를 받을 수 있다.
+ 인터셉터는 어떤 modelAndView가 반환되는지 응답 정보를 받을 수 있다.

<br>

### 스프링 인터셉터 호출 흐름

<br>

#### 스프링 인터셉터 호출 정상 흐름
![인터셉터_호출정상흐름.png](src%2Fmain%2Fresources%2Fimages%2F%EC%9D%B8%ED%84%B0%EC%85%89%ED%84%B0_%ED%98%B8%EC%B6%9C%EC%A0%95%EC%83%81%ED%9D%90%EB%A6%84.png)

+ `preHandle` : 컨트롤러 호출 전(정확히는 핸들러 어댑터 호출 전)에 호출된다.
  + 응답값이 true이면 다음으로 진행한다. 
  + false이면 나머지 인터셉터와 핸들러 어댑처가 호출되지 않는다.(다음으로 진행 X)
+ `postHandle` : 컨트롤러 호출 후(정확히는 핸들러 어댑터 호출 후)에 호출된다.
+ `afterCompletion` : 뷰가 랜더링 된 이후에 호출된다. 
  + 정상 흐름에서는 예외(`ex`)가 null이다.

<br>

#### 스프링 인터셉터 예외 발생 흐름
![인터셉터_예외상황.png](src%2Fmain%2Fresources%2Fimages%2F%EC%9D%B8%ED%84%B0%EC%85%89%ED%84%B0_%EC%98%88%EC%99%B8%EC%83%81%ED%99%A9.png)
+ `preHandle` : 컨트롤러 호출 전에 호출된다.
+ `postHandle` : 컨트롤러에서 예외가 발생하면 `postHandle`은 호출되지 않는다.
+ `afterCompletion` : `afterCompletion` 은 항상 호출된다.
  + 예외가 발생하면 `afterCompletion()`에 예외 정보(`ex`)를 포함해서 호출된다.
  + 예외(`ex`)를 파라미터로 받아서 어떤 예외가 발생했는지 로그로 출력할 수 있다.
  + 예외가 발생하면 `postHandle()`는 호출되지 않으므로 예외와 무관하게 공통 처리를 하려면 `afterCompletion()`을 사용해야 한다.

<br><br>

### 예시1 : 요청 로그 인터셉터

`LogInterceptor` - 요청 로그 인터셉터

```java
@Slf4j
public class LogInterceptor implements HandlerInterceptor {

  public static final String LOG_ID = "logId";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    String requestURI = request.getRequestURI();
    String uuid = UUID.randomUUID().toString();
    request.setAttribute(LOG_ID, uuid);

    // @RequestMapping: HandlerMethod
    // 정적 리소스: ResourceHttpRequestHandler
    if (handler instanceof HandlerMethod) {
      HandlerMethod hm = (HandlerMethod) handler;//호출할 컨트롤러 메서드의 모든 정보가 포함되어 있다.
    }

    log.info("REQUEST [{}][{}][{}]", uuid, requestURI, handler);
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    log.info("postHandle [{}]", modelAndView);
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    String requestURI = request.getRequestURI();
    String logId = (String) request.getAttribute(LOG_ID);
    log.info("RESPONSE [{}][{}][{}]", logId, requestURI, handler);
    if (ex != null) log.error("afterCompletion error!!", ex);
  }
}
```
+ `String uuid = UUID.randomUUID().toString()` :  요청 로그를 구분하기 위한 `uuid`를 생성한다.
+ `request.setAttribute(LOG_ID, uuid)`
  + 스프링 인터셉터는 호출 시점이 완전히 분리되어 있다. 
  + 따라서 `preHandle`에서 지정한 값을 `postHandle`, `afterCompletion` 에서 함께 사용하려면 어딘 가에 담아두어야 한다. `LogInterceptor` 도 싱글톤 처럼 사용되기 때문에 맴버변수를 사용하면 위험하다. 따라서 `request` 에 담아두었다.
  + 이 값은 `afterCompletion`에서 `request.getAttribute(LOG_ID)`로 찾아서 사용한다.
+ `return true` : `true`면 정상 호출이다. 다음 인터셉터나 컨트롤러가 호출된다.
+ HandlerMethod : @Controller, @RequestMapping을 활용한 핸들러 매핑을 사용하는데, 이 경우 핸들러 정보로 HandlerMethod가 넘어온다.
+ ResourceHttpRequestHandler: @Controller가 아닌 /resources/static와 같은 정적 리소스가 호출 되는 경우 ResourceHttpRequestHanlder가 핸들러 정보로 넘어온다.

<br>

`WebConfig` - 인터셉터 등록
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error");
    }
    //...
}
```
+ `registry.addInterceptor(new LogInterceptor())` : 인터셉터를 등록한다.
+ `order(1)` : 인터셉터의 호출 순서를 지정한다. 낮을수록 먼저 호출된다.
+ `addPathPatterns("/**")` : 인터셉터를 적용할 URL 패턴을 지정한다. 
+ `excludePathPatterns("/css/**", "/*.ico", "/error")` : 인터셉터에서 제외할 패턴을 지정한다.

<br><br>

### 예시2 : 인증 체크

`LoginCheckInterceptor`

```java
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override 
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI(); log.info("인증 체크 인터셉터 실행 {}", requestURI);
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
            log.info("미인증 사용자 요청");
            //로그인으로 redirect
            response.sendRedirect("/login?redirectURL=" + requestURI);
            return false;
        }
        return true;
    }
}
```
인증은 컨트롤러 호출 전에만 호출하면 되기 때문에 preHandle만 구현하면 된다.

<br>

`WebConfig` - 인터셉터 등록

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Log Interceptor
        // ...
      
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPattern("/**")
                .excludePathPatterns(
                        "/", "/members/add", "/login", "/logout",
                        "/css/**", "/*.ico", "/error"
                );
    }
}
```
+ 로그인 체크는 기본적으로 모든 경로에 해당 인터셉터를 조회한다.(/**)
+ 홈(/), 회원가입(/memebers/add), 로그인(/login), 리소스 조회(/css/**), 오류(/error)와 같은 부분은 로그인 체크 인터셉터를 적용하지 않는다.
