# JPA1

## API 만들기

### API를 만들 때에는 항상 Entity를 받으면 안된다.
   1. 어떤 환경에선 Entity의 필드에 @NotEmpty가 필요할 수도 필요하지 않을 수도 있다.
   2. 화면 Validation 로직에 의해 Entity가 영향을 받을 수 있다.
   3. Entity의 필드명을 바꿀 경우 API 스펙 자체가 바뀌어버릴 수 있다. 
   4. 개발자의 입장에서 API를 열어보지 않은 한 Entity의 값이 있는지 없는지 확인할 수 있다.

### 엔터티를 직접적으로 노출하기 보단 DTO를 사용해라!

1. Entity의 필드명이 달라져도 이전과 동일하게 처리할 수 있다. 

        @PostMapping("/api/v2/members")
        public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
            Member member = new Member();
            // member의 name이 username으로 바뀌어도 API의 스펙이 바뀌지 않게 된다.
            member.setUserName(request.getName());  

            Long id = memberService.join(member);
            return new CreateMemberResponse(id);
        }    
        
        @Data
        static class CreateMemberRequest{
            private String name;
        }
 
2. Entity가 어떤 필드명을 가지고 있는지 명확하게 알 수 있다.
3. Entity와 Presentation 계층을 위한 로직을 분리할 수 있다.
