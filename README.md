# jpa-study
김영한님   [실전! 스프링 부트와 JPA 활용1 - 웹 애플리케이션 개발](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-JPA-%ED%99%9C%EC%9A%A9-1/dashboard)




⭐  spring-boot-devtools 라이브러리를 추가하면, html 파일을 컴파일만 해주면 서버 재시작 없이
View 파일 변경이 가능하다.


⭐   **@Enumerated**  

>(Optional) The type used in mapping an enum type. 

enum 값을 어떤 형태로 저장할지 
- EnumType.STRING : 문자열 그대로 저장
- EnumType.ORDINAL : enum의 순서값(int)을 저장



⭐  **@ManyToMany**
    중간 테이블( 각각의 id만 모아둔 테이블 )에 컬럼을 추가 할 수 없고, 세밀하게 쿼리를 실행하기 어렵기 때문에 실무에서 사용하기에는 한계가 있다.
 ⇒ **중간 엔티티를 만들고 @ManyToOne , @OneToMany 로 매핑**해서 사용하자.
 
 
 ⭐  **@XToOne** ( @OneToOne, @ManyToOne ) → 기본이 즉시로딩,,

- 모든 연관 관계는 **지연로딩(LAZY)**으로 설정해야함
- 즉시로딩(EAGER)은 예측이 어렵고 어떤 SQL이 실행될지 추척이 어려움
- 연관된 엔티티를 함께 DB에서 조회해야 하면, fetch join 또는 엔티티 그래프 기능을 사용한다.

⭐  엔티티에는 가급적 **Setter**를 사용하지 말자

⭐  **@Transactional** : 트랜잭션, 영속성 컨텍스트

- readOnly=true : 데이터의 변경이 없는 읽기 전용 메서드에 사용, 영속성 컨텍스트를 플러시 하지 않으므로 약간의 성능 향상(읽기 전용에는 다 적용)
- 데이터베이스 드라이버가 지원하면 DB에서 성능 향상
