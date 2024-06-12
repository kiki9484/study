package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    /*
     * // @PersistenceContext private EntityManager em;
     * 스프링 부트 라이브러리를 사용하면 @PersistenceContext를 @Autowired로 바꿀 수 있다.
     * 그러므로 맴버변수를 final로 바꾸고, @RequiredArgsConstructor를 넣어줄 수 있다.
     */
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member); // 영속성 컨텍스트에 member가 올라간다.
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    // jpql 작성 - 엔티티 객체를 대상으로 쿼리를 한다.
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
