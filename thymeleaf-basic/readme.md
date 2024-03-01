## 프로젝트 환경설정

### Spring 파일 만들기
#### 프로젝트 선택
  + Project: Gradle-Groovy Project
  + Language: Java
  + Spring boot: 3.2.3 
#### Dependencies
  + Spring Web
  + Lombok
  + Thymeleaf
  + Validation
<br><br>

### lombok 적용하기
1. Preferences -> plugin -> lombok 검색
2. Preferences -> Annotation Processors -> Enable annotation processing 체크

### Postman 설치
https://www.postman.com/downloads

****

## 타임리프
### 타임리프 사용서
+ 공식 사이트: https://www.thymeleaf.org/
+ 공식 메뉴얼 - 기본 기능: https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html
+ 공식 메뉴얼 - 스프링 통합: https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html
<br><br>

### 타임리프 특징
+ 서버 사이드 HTML 렌더링 (SSR): 백엔드 서버에서 HTML을 동적으로 렌더링 하는 용도로 사용된다.
+ 네츄럴 템플릿: 타임리프로 작성한 파일은 HTML을 유지하기 때문에 웹 브라우저에서 파일을 직접 열어도 내용을 확인할 수 있고, 서 버를 통해 뷰 템플릿을 거치면 동적으로 변경된 결과를 확인할 수 있다.
+ 스프링 통합 지원: 스프링의 다양한 기능을 편리하게 사용할 수 있게 지원한다.
<br><br>

### 타임리프 기본 기능
#### 타임리프 사용 선언: \<html xmlns:th="http://www.thymeleaf.org">
#### 기본 표현식
+ 간단한 표현
  + 변수 표현식: ${...}
  + 선택 변수 표현식: *{...}
  + 메시지 표현식: #{...}
  + 링크 URL 표현식: @{...}
  + 조각 표현식: ~{...}
+ 리터럴
  + 텍스트: 'one text', 'Another one!',...
  + 숫자: 0, 34, 3.0, 12.3,...
  + 불린: true, false
  + 널: null
  + 리터럴 토큰: one, sometext, main,...
+ 문자 연산:
  + 문자합치기:+
  + 리터럴 대체: |The name is ${name}|
+ 산술 연산:
  + Binary operators: +, -,  *, /, %
  + Minus sign (unary operator): -
+ 불린 연산:
  + Binary operators: and, or 
  + Boolean negation (unary operator): !, not • 비교와 동등:
  + 비교:>,<,>=,<=(gt,lt,ge,le)
  + 동등 연산: ==, != (eq, ne) • 조건 연산:
  + If-then: (if) ? (then)

참고: https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#standard-expression-syntax
<br><br>

### Escape와 Unescape
#### Escape
+ 웹 브라우저는 <를 HTML 태그의 시작으로 인식한다. 따라서 <를 태그의 시작이 아니라 문자료 표현할 수 있는 방법이 필요한데, 이것을 HTML 엔터티라 한다.
+ HTML에서 사용하는 특수 문자를 HTML 엔터티로 변경하는 것을 escape라 한다.
+ th:text, [[...]]은 escape를 제공한다.
#### UnEscape
+ th:utext, [(...)]을 통해 unescape를 제공한다.
+ th:inline="none": 이 태그 안에서는 타임리프가 해석하지 말라는 옵션이다.
<br><br>

### 변수 - SpringEL
> 변수 표현식: ${...}

<br><br>

### 유틸리티 객체와 날짜
#### 타임리프 유틸리지 객체들
+ `#message` : 메시지, 국제화 처리
+ `#dates` : `java.util.Date` 서식 지원
+ `#calendars` : `java.util.Calendar` 서식 지원
+ `#temporals` : 자바8 날짜 서식 지원
+ `#uris` : URI 이스케이프 지원
+ `#numbers` : 숫자 서식 지원
+ `#strings` : 문자 관련 편의 기능
+ `#objects` : 객체 관련 기능 제공
+ `#bools` : boolean 관련 기능 제공
+ `#arrays` : 배열 관련 기능 제공
+ `#lists` , `#sets` , `#maps` : 컬렉션 관련 기능 제공

#### 타임리프 유틸리지 객체
https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#expression-utility-objects

#### 유틸리티 객체 예시
https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#appendix-b-expression-utility-objects
<br><br>

### URL 링크
> URL 링크 표현식: @{...}

#### 단순한 URL
    `@{/hello}` -> `/hello`
#### 쿼리 파라미터
    `@{/hello(param1=${param1}, param2=${param2})}`-> `/hello?param1=data1&param2=data2`
  + `()` 에 있는 부분은 쿼리 파라미터로 처리된다.
#### 경로 변수
    `@{/hello/{param1}/{param2}(param1=${param1}, param2=${param2})}` -> `/hello/data1/data2`
  + URL 경로상에 변수가 있으면 `()` 부분은 경로 변수로 처리된다.
#### 경로 변수 + 쿼리 파라미터
    `@{/hello/{param1}(param1=${param1}, param2=${param2})}` -> `/hello/data1?param2=data2`
+ 경로 변수와 쿼리 파라미터를 함께 사용할 수 있다.
+ 상대경로, 절대경로, 프로토콜 기준을 표현할 수 도 있다.
  + `/hello` : 절대 경로
  + `hello` : 상대 경로

참고: https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#link-urls
<br><br>

### 리터럴
#### 리터럴: 소스 코드상에 고정된 값
+ 문자: 'hello' (타임리프에서 문자 리터럴은 항상 '(작은 따음표)로 감싸야 한다.)
+ 숫자: 10
+ 불린: true, false
+ null: null
#### 리터럴 대체
    "|...|" 
+ 예시: \<span th:text="|hello ${data}|">\</span>
<br><br>

### 연산
#### 비교연산
+ HTML 엔티티를 사용하는 부분을 주의해야 한다.
+ \>(gt), \>(lt), \>=(ge), \<=(le), \!(not), \==(eq), \!=(neq, ne)
#### 조건식
+ 자바의 조건식과 유사하다.
#### Elvis 연산자
> 표현식: ${변수}?: '설명'
+ 조건식의 편의 버전
+ \<span th:text="${data}?: '데이터가 없습니다.'">\</span>
#### No-Operation
    <span th:text="${data}?: _">HTML 내용\</span>
+ _ 인 경우 타임리프가 실행되지 않는 것처럼 동작하기 때문에 HTML의 내용 그대로 활용할 수 있다.

  <br><br>

### 속성
> 속성 표현식: th:*

타임리프가 기존 속성을 th:*로 지정한 속성으로 대체한다. 기존 속성이 없다면 새로 만든다.

#### 속성 추가
+ th:attrappend: 속성 값의 뒤에 값을 추가한다.
+ th:attrprepend: 속성 값의 앞에 값을 추가한다.
+ th:classappend: class 속성에 추가한다.

##### checked 처리
+ \<input type="checkbox" name="active" checked="false"/>
+ HTML:  checked 속성의 값과 관계없이 체크가 된다.
+ 타임라프: th:checked는 값이 false인 경우 checked 속성 자체를 제거한다.
  + 타임리프 렌더링 후: \<input type="checkbox" name="activ"/>
<br><br>

### 반복
> 반복 표현식: th:each

#### 반복 기능: \<tr th:each="user : ${users}">
#### 반복 상태 유지: \<tr th:each="user, userStat : ${users}">
  + 두번째 파라미터(변수명 + Stat)은 생략 가능하다.
+ 반복 상태 유지 기능
  + index: 0부터 시작하는 값
  + count: 1부터 시작하는 값
  + size: 전체 사이즈
  + even, odd: 홀수, 짝수 여부(boolean)
  + first, last: 처음, 마지막 여부(boolean)
  + current: 현재 객체
<br><br>

### 조건부 평가
> 조건식: if, unless, switch

#### if, unless
+ 타임리프는 해당 조건이 맞지 않으면 태그 자체를 랜더링하지 않는다.
+ \<span th:text="'미성년자'" th:if="${user.age lt 20}">\</span>의 조건이 false인 경우, <span>...<span>부분 자체가 랜더링 되지 않고 사라진다.

#### switch
+ *은 만족하는 조건이 없을 때 사용하는 디폴트이다.
<br><br>

### 주석
+ 표준 HTML 주석: 자바스크립트 표준 HTML 주석은 타임리프가 랜더링 하지 않고, 그대로 남겨둔다.
+ 타임리프 파서 주석: 타임리프의 주석이다. 랜더링에서 주석 부분을 제거한다.
+ 타임리프 프로토타입 주석: HTML파일을 열어보면 주석처리가 되지만, 타임리프를 랜더링 한 경우에는 보인다.
<br><br>

### 블록
> 블록 표현식: &lt;th:block&gt;
+ 타임리프의 유일한 자체 태그이다.
+ html 태그들을 묶어서 처리할 때 사용한다.
<br><br>

### 템플릿 조각
+ 타임리프는 여러 페이지에서 함께 사용하는 영역들을 공통으로 사용하기 위해 템플릿 조각을 지원한다.
+ template/fragment/footer::copy
  + template/fragment/footer.html 템플릿에 있는 th:fragment="copy"라는 부분을 템플릿 조각으로 가져와서 사용한다.
+ 부분 포함 insert: th:insert를 사용하면 현재 태그 내부에 추가한다.
+ 부분 포함 replace: th:replace를 사용하면 현재 태그를 대체한다.
+ 부분 포함 단순 표현식: ~{...}를 사용하는 것이 원칙이지만 코드가 단순하면 생략할 수 있다.
+ 파라미터 사용: 파라미터를 전달하여 동적으로 조각을 랜더링할 수 있다.
<br><br>

### 템플릿 레이아웃
코드 조각을 레이아웃에 넘겨서 사용하는 방법
<br><br>
