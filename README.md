# jpa-study
김영한님   [실전! 스프링 부트와 JPA 활용1 - 웹 애플리케이션 개발](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-JPA-%ED%99%9C%EC%9A%A9-1/dashboard)




⭐  spring-boot-devtools 라이브러리를 추가하면, html 파일을 컴파일만 해주면 서버 재시작 없이
View 파일 변경이 가능하다.


⭐  @Enumerated 

>(Optional) The type used in mapping an enum type. 

enum 값을 어떤 형태로 저장할지 
- EnumType.STRING : 문자열 그대로 저장
- EnumType.ORDINAL : enum의 순서값(int)을 저장



⭐  **@ManyToMany**
    중간 테이블( 각각의 id만 모아둔 테이블 )에 컬럼을 추가 할 수 없고, 세밀하게 쿼리를 실행하기 어렵기 때문에 실무에서 사용하기에는 한계가 있다.
 ⇒ **중간 엔티티를 만들고 @ManyToOne , @OneToMany 로 매핑**해서 사용하자.
