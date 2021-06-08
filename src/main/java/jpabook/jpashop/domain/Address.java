package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable // 밸류 클래스 , 컬럼을 하나의 객체로 사용
@Getter
public class Address {

    private String city;
    private String street;
    private String  zipcode;
}
