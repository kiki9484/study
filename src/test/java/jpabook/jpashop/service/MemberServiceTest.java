package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/*
 * 1. 단위 테스트가 아닌 스프링이랑 intigration한 테스트 코드 작성
 *   - @RunWith(SrpingRunner.class) @SprintBootTest가 있어야 한다.
 * 2. db 데이터를 변경해야 된다.
 *   - @Transactional가 필요하다.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
/*
 * 테스트 파일에 @Transactional이 있으면 기본적으로 트랜잭션을 커밋하지 않고, 롤백을 하게 된다.
 * 스프링이 롤백을 하면 JPA입장에서는 insert 쿼리를 db에 보낼 필요가 없기 때문에 insert쿼리가 보이지 않게 된다.
 * 정확하게 말하면 영속성 컨텍스트를 flush하지 않는다.
 *
 * 쿼리 문을 db에 보내는 방법
 *   1. db에 insert 쿼리 문을 보내는 것을 보고 싶으면 @Rollback(value = false)을 추가하면 된다.
 *      1_1. @Rollback(false)를 사용하고 h2에 들어가서 실제로 데이터가 들어가는지 확인하는 것이 초급자일 때는 좋다.
 *   2. @Autowired EntityManager em; 을 한 후에 em.flush()로 강제로 쿼리를 보낸다.
 */
@Transactional
public class MemberServiceTest {
    /*
     * spring이 들어가는 테스트 코드이기 때문에 간단하게 필드명에 @Autowired를 해도 된다.
     *   - @Autowired MemberService memberService;
     *   - @Autowired MemberRepository memberRepository;
     */
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
//    @Autowired EntityManager em;
    @Test
    @Rollback(value = false) // insert문을 콘솔에서 확인할 수 있으며, db에 데이터가 들어갔는지 시각적으로 확인할 수 있다.
    public void 회원가입() {
        // given
        Member member = new Member();
        member.setName("park");

        // when
        Long savedId = memberService.join(member);

        // then
//        em.flush(); // 콘솔에서 insert문을 확인할 수 있는 또 다른 방법
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test(expected = IllegalStateException.class) // 예외가 발생 한 것이 IllegalStateException.class이면 된다.
    public void 중복_회원_예외() {
        // given
        Member member1 = new Member();
        member1.setName("park1");

        Member member2 = new Member();
        member2.setName("park1");

        // when
        memberService.join(member1);
        memberService.join(member2); // 같은 이름이므로 예외가 발생해야 한다.

        // then
        fail("예외가 발생해야 한다.");
    }
}