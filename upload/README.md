## 파일 업로드

<br>

### HTML 폼 전송 방식 
`application/x-www-form-urlencoded` 
+ `application/x-www-form-urlencoded` 방식은 HTML 폼 데이터를 서버로 전송하는 가장 기본적인 방법이다. 
+ Form 태그에 별도의 `enctype` 옵션이 없으면 웹 브라우저는 요청 HTTP 메시지의 헤더에 다음 내용을 추가한다.
  + `Content-Type: application/x-www-form-urlencoded`
+ 그리고 폼에 입력한 전송할 항목을 HTTP Body에 문자로 `username=kim&age=20` 와 같이 `&`로 구분해서 전송한다.

<br>

`multipart/form-data`
+ 파일은 문자가 아니라 바이너리 데이터를 전송해야 한다.
+ 문자를 전송하는 방식으로는 파일을 전송하기는 어렵다. 또한, 보통 폼을 전송할 때 파일만 전송하는 것이 아니다.
+ 문자와 바이너리를 동시에 전송해야 하는 상황에 사용한다.
+ `multipart/form-data` 방식은 다른 종류의 여러 파일과 폼의 내용 함께 전송할 수 있다.

<br><br>

### 스프링과 파일 업로드

스프링은 `MultipartFile` 이라는 인터페이스로 멀티파트 파일을 매우 편리하게 지원한다.

<br>

#### MultipartFile 주요 메서드 
+ `file.getOriginalFilename()` : 업로드 파일 명 
+ `file.transferTo(...)` : 파일 저장

<br>

### 파일 업로드 예시

#### SpringUploadController
```java
@Slf4j
@Controller
@RequestMapping("/spring")
public class SpringUploadController {
    @Value("${file.dir}")
    private String fileDir;
    
    @GetMapping("/upload")
    public String newFile() {
        return "upload-form";
    }
    
    @PostMapping("/upload")
    public String saveFile(@RequestParam String itemName, @RequestParam MultipartFile file, HttpServletRequest request) throws IOException {
        log.info("request={}", request);
        log.info("itemName={}", itemName);
        log.info("multipartFile={}", file);
        
        if (!file.isEmpty()) {
            String fullPath = fileDir + file.getOriginalFilename(); log.info("파일 저장 fullPath={}", fullPath); file.transferTo(new File(fullPath));
        }
       return "upload-form";
    }
}
```
+ 업로드하는 HTML Form의 name에 맞추어 `@RequestParam` 을 적용하면 된다. 
+ 추가로 `@ModelAttribute`에서도 `MultipartFile`을 동일하게 사용할 수 있다.
<br>



