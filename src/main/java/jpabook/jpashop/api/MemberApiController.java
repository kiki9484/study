package jpabook.jpashop.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController // @RestController = @Controller + @ResponseBody
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    /* 비추 */
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        /* @RequestBody Member: json으로 온 body를 Member에 mapping해서 넣어준다. */
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /* DTO를 사용하는 해당 방법 사용할 것*/
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable Long id, @RequestBody @Valid updateMemberRequest request) {
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    /* 비추 -
     * 1. 배열[]로 넘기기 때문에 확장성이 좋지 않다.
     * 2. Entity가 노출되므로 좋지 않다
     */
    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result membersV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(MemberApiController::apply)
                .toList();
        return new Result(collect.size(), collect);
    }

    private static MemberDto apply(Member m) {
        return new MemberDto(m.getName());
    }

    @Data
    static class CreateMemberRequest {
        @NotEmpty
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    static class updateMemberRequest {
        private String name;
    }

    @Data
    @RequiredArgsConstructor
    static class UpdateMemberResponse {
        private final Long id;
        private final String name;
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    // 전체 Member의 개수와 name만 json으로 반환하는 DTO
    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }
}

