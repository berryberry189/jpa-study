# jpa-study
김영한님   [실전! 스프링 부트와 JPA 활용1 - 웹 애플리케이션 개발](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-JPA-%ED%99%9C%EC%9A%A9-1/dashboard)

[실전! 스프링 부트와 JPA 활용2 - API 개발과 성능 최적화](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-JPA-API%EA%B0%9C%EB%B0%9C-%EC%84%B1%EB%8A%A5%EC%B5%9C%EC%A0%81%ED%99%94/dashboard)


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

- 모든 연관 관계는 **지연로딩(LAZY)** 으로 설정해야함 
- 즉시로딩(EAGER)은 예측이 어렵고 어떤 SQL이 실행될지 추척이 어려움
- 연관된 엔티티를 함께 DB에서 조회해야 하면, fetch join 또는 엔티티 그래프 기능을 사용한다.

⭐  엔티티에는 가급적 **Setter**를 사용하지 말자

⭐  **@Transactional** : 트랜잭션, 영속성 컨텍스트

- readOnly=true : 데이터의 변경이 없는 읽기 전용 메서드에 사용, 영속성 컨텍스트를 플러시 하지 않으므로 약간의 성능 향상(읽기 전용에는 다 적용)
- 데이터베이스 드라이버가 지원하면 DB에서 성능 향상
- SpringBootTest일 경우,  SpringBoot가 알아서 RollBack처리한다 ⇒ RollBack처리 하기 싫으면 @RollBack(false)를 써주면 된다.

⭐  **엔티티 매니저 ( EntityManager )**

- 엔티티를 저장, 수정, 삭제, 조회등 엔티티와 관련된 작업을 수행

```java
EntityManager em = emf.createEntityManager();

em.find();    // 엔티티 조회
em.persist(); // 엔티티 저장
em.remove();  // 엔티티 삭제
em.flush();   // 영속성 컨텍스트 내용을 데이터베이스에 반영
em.detach();  // 엔티티를 준영속 상태로 전환
em.merge();   // 준영속 상태의 엔티티를 영속상태로 변경
em.clear();   // 영속성 컨텍스트 초기화
em.close();   // 영속성 컨텍스트 종료

```

⭐  **영속성 컨텍스트 ( Persistence Context )**

- 엔티티 매니저는 내부에 영속성 컨텍스트를 두어서 엔티티들을 관리한다.
- 영속성 컨텍스트는 **엔티티를 영구히 저장하는 환경**이다.  →  em.persist(entity);
- 엔티티는 flush() 호출 시 데이터베이스에 반영된다.   
   ⇒  1. em.flush(entity); 직접호출.  
      2. 트랜잭션 커밋 시 flush 자동 호출.    
      3. JPQL 쿼리 실행 시 flush 자동 호출. 


# API 개발

⭐ api 요청과 응답은 entity를 그대로 쓰면 안된다!
   api 스펙에 맞는 별도의 dto 생성이 필수이다. => 유지보수성 ↑

⭐  실무에서는 엔티티를 API 스펙에 노출하면 안된다!

⭐  **PUT**은 전체 업데이트, 부분 업데이트의 경우에는 **PATCH** or **POST** 사용이 REST 스타일에 맞다.

⭐  **N+1문제**가 발생하는 경우 **페치조인**으로 해결한다.

```java
public List<Order> findAllWithMemberDelivery() {
	 return em.createQuery(
			"select o from Order o" +
			" join fetch o.member m" +
			" join fetch o.delivery d", Order.class)
			.getResultList();
}
```

⭐  **쿼리 방식 선택 권장 순서**

1. 우선 엔티티를 DTO로 변환하는 방법을 선택한다.
2. 필요하면 페치 조인으로 성능을 최적화 한다. 대부분의 성능 이슈가 해결된다.
3. 그래도 안되면 DTO로 직접 조회하는 방법을 사용한다.
4. 최후의 방법은 JPA가 제공하는 네이티브 SQL이나 스프링 JDBC Template을 사용해서 SQL을 직접사용한다.

### ⭐  **컬렉션 조회 최적화**

⇒ ex ) Order 기준으로 OrderItem(OneToMany) , Item이 필요

- DTO 예제
- 전부 페치조인 예제 but, 페이징 불가능

### ⭐  **페이징과 한계 돌파**

- 먼저 ToOne(OneToOne, ManyToOne) 관계를 모두 페치조인 한다

    ⇒ ToOne 관계는 row수를 증가시키지 않으므로 페이징 쿼리에 영향을 주지 않는다.

- 컬렉션은 지연 로딩으로 조회한다
- hibernate.default_batch_fetch_size , @BatchSize 를 적용 ⇒ **설정한 size 만큼 IN 쿼리로 조회**
    - hibernate.default_batch_fetch_size: 글로벌 설정 ( application.yml 등 ) ⇒ **100~1000권장**
    - @BatchSize: 개별 최적화( 컬렉션은 컬렉션 필드에, 엔티티는 엔티티 클래스에 적용 )


### ⭐  OSIV ( Open Session In View )

### spring.jpa.open-in-view : true (기본값)

트랜잭션을 시작할때 영속성 컨텍스트가 DB 컨넥션을 가져온다.

open-in-view가 켜져있으면 트랜잭션 끝날때 까지 DB로 반환을 안한다.

트랜잭션이 끝나더라도 response가 나갈 때 까지 **영속성 컨텍스트를 유지**하고 response가 나갈 때 DB로 반환한다 ( **⇒ 지연로딩** )

하지만 너무 오랫동안 DB 커넥션을 가지고 있으므로 트래픽이 중요한 애플리케이션에서는 커넥션이 모자랄 수 있다.

이것이 결국 장애로 이어진다.

### spring.jpa.open-in-view : false

OSIV를 끄면 **트랜잭션을 종료할 때 영속성 컨텍스트를 닫고**, 데이터베이스 커넥션도 반환한다

⇒ 커넥션 리소스를 낭비하지 않는다.

OSIV를 끄면 **모든 지연로딩을 트랜잭션 안에서 처리**해야 한다. ⇒ 지연 로딩코드를 트랜잭션 안으로 넣어야 하는 단점이 있다. 

그리고 view template에서 지연로딩이 동작하지않는다. 

결론적으로 트랜잭션이 끝나기 전에 지연 로딩을 강제로 호출해 두어야 한다.
