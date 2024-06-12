package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*
 * @Transactional은 스프링이 제공하는 @Transactional사용하는 것이 더 좋다.
 * 조회에는 readOnly = true를 넣는것이 좋다.
 * 조회가 많음으로 클래스 전체에 readOnly = true를 넣어주고, 변경이 있는 메소드에만 따로 @Transactional만 써준다.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor // 생성자 주입 방식을 써라!
public class MemberService {
    private final MemberRepository memberRepository; // 변경할 일이 없으므로 final를 추가한다.

    // 회원 가입
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    /* 중복 회원이면 runtime exception 발생시키는 메서드
     * 동시에 같은 이름으로 회원가입을 하게 되면(멀티쓰레드 환경) 이 메서드를 통과하면서 save(member)가 호출될 수도 있다.
     * 그러므로, 비즈니스 로직 예외 뿐만 아니라 멀티쓰레드 환경을 고려해 데이터베이스의 member_name를 unique로 잡는 것이 좋다.
     */
    private void validateDuplicateMember(Member member){
        List<Member> findMembers = memberRepository.findByName(member.getName());// 같은 이름이 있는지 찾기
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다."); // 런타임 예외 발생시키기
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    /* 1. 변경 감지 방법
     * command와 query가 같이 있는 것은 좋지 않으므로 Member를 반환하지 않고, void나 id정도만 반환하는 것이 좋다.
     */
    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
