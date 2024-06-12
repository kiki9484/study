## 타임리프 스프링 통합

### 메뉴얼
+ 기본 메뉴얼: https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html
+ 스프링 통합 메뉴얼: https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html
<br><br>

### 스프링 통합으로 추가되는 기능들
+ 스프링의 SpringEL 문법 추가
+ ${@myBean.doSomethin()}처럼 스프링 빈 호출 지원
+ 편리한 폼 관리를 위한 추가 속성
  + th:object
  + th:field, th:errors, th:errorclass
+ 폼 컴포넌트 기능
  + checkbox, radio button, List 편리하게 사용 기능 지원
+ 스프링의 메시지, 국제화 기능의 편리한 통합
+ 스프리의 검증, 오류 처리 통합
+ 스프링의 변환 서비스 통합(ConversionService)
<br><br>

### 입력 폼 처리
+ th:object : 커맨드 객체를 지정한다.
+ *{...} : th:object에서 선택한 객체에 접근한다.
+ th:field : HTML 태그의 id, name, value 속성을 자동으로 처리해준다.
  + 랜더링 전: \<input type="text" th:field="*{itemName}"/>
  + 랜더링 후: \<input type="text" id="itemName" name="itemName" th:value="*{itemName}"/>
<br><br>


### 입력 폼 처리 예시
#### th:object="${item}"
+ 사용할 객체를 지정한다. 
+ 선택 변수 식 *{...}를 적용할 수 있게 된다.
#### th:field="${itemName}"
+ th:object로 item을 선택했기 때문에 ${item.itemName}을 줄여서 *{itemName}으로 쓸 수 있다.
+ th:field는 id, name, value 속성을 모두 자동으로 만들어준다.
  + id : th:field에서 지정한 변수 이름과 같다.(id = "itemName")
  + name : th:field에서 지정한 변수 이름과 같다.(name = "itemName")
  + value : th:field에서 지정한 변수의 값을 사용한다.(value = "")
#### 랜더링 전, 후
+ 랜더링 전: \<input type="text" id="itemName" th:field="*{itemName} class="form-control" placeholder="이름을 입력하세요">
+ 랜더링 후: \<input type="text" id="itemName" class="form-control" placeholder="이름을 입력하세요" name="itemName" value="">
<br><br>


### 체크 박스 - 단일 1
+ HTML에서 체크 박스를 선택하고 폼을 전송하면 open=on 이라는 값이 넘어간다.(스프링이 on이라는 문자를 true로 바꿔준다)
+ HTML에서 체크 박스를 선택하지 않고 폼을 전송하면 open 필드가 서버로 전송되지 않는다.
+ HTML checkbox는 선택이 안되면 클라이언트에서 서버로 값 자체를 보내지 않는다. 
+ 사용자가 수정 시, 체크되어 있는 값을 체크 해제해도 저장 시 값이 아무 것도 넘어가지 않기 때문에 서버에서는 값이 오지 않은 것으로 판단해서 값을 변경하지 않을 수 있다.
+ 스프링 MVC는 히든 필드를 하나 만들어서 \_open처럼 기존 체크 박스 이름 앞에 언더스코어(_)를 붙여서 전송하면 체크를 해제했다고 인식한다.
  + 체크 해제를 인식하기 위한 히든 필드: \<input type="hidden" name="_open" value="on"/>
  + 체크 박스 체크: open=on&_open=on
    + 체크 박스를 체크하면 스프링 MVC가 open에 있는 값을 확인하고 사용한다. 이 때, _open은 무시한다.
  + 체크 박스 미체크: _open=on
    + 체크 박스를 체크하지 않으면 스프링 MVC가 _open만 있는 것을 확인하고, open의 값이 체크되지 않았다고 인식한다.
  + 타임리프를 사용하면 체크 박스의 히든 필드와 관련된 부분도 함께 해결해준다.
  + 타임리프의 th:field를 사용하면 checked="checked"경우 체크를 자동으로 처리해준다.
<br><br>

### 체크 박스 - 멀티 1
+ ids.prev(...), ids.next(...) : 동적으로 생성되는 id값
  + 멀티 체크박스는 같은 이름의 여러 체크박스를 만들 수 있다.
  + 생성된 HTML 태그 속성에서 name은 같아도 되지만, id는 모두 달라야 한다.
  + 타임리프는 체크박스를 each루프 안에서 반복해서 만들 때 임의로 1,2,3 숫자를 뒤에 붙여준다.
  + HTML의 id가 타임리프에 의해 동적으로 만들어지기 때문에 <label for="id 값">으로 label의 대상이 되는 id값을 임의로 지정할 수 없다. 
  + 타임리프는 ids.prev(...), ids.next(...)을 제공해서 동적으로 생성되는 id값을 사용할 수 있도록 한다.
+ 타임리프의 체크 확인
  + 멀티 체크 박스에서 등록 지역을 선택해서 저장하면, 조회 시 checked속성이 추가된 것을 볼 수 있다.
  + 타임리프는 th:field에 지정한 값과 th:value 값을 비교해서 체크를 자동으로 처리해준다.
  
<br><br>

### 라디오 버튼
+ 라디오 버튼은 체크 박스와 다르게 수정 시에도 항상 하나를 선택하도록 되어 있으므로 별도의 히든 필드를 사용할 필요가 없다.
+ th:field을 사용하면 된다.
<br><br>


### 셀렉트 박스
+ 셀렉트 박스는 여러 선택지 중에 하나를 선택할 때 사용할 수 있다.
+ th:field를 사용하면 된다.
<br><br>


### 참고
#### HTTP 요청 메시지 로깅
+ application.properties 파일에 logging.level.org.apache.coyote.http11=debug 추가