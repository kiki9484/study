package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    /* JPA 구현 라이브러리가 객체를 생성할 때 리플랙션이나 프록시 같은 기술을 사용하기 때문에 기본 생성자가 필요하다.
     * public보단 protected로 설정하는 것이 더 안전하다.
     */
    protected Address(){}
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
