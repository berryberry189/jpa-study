package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter @Setter
public class Order {

    @Id
    @GeneratedValue
    @Column(name="order_id")
    private Long id;

    // 연관 관계의 주인
    @ManyToOne
    @JoinColumn(name="member_id") // fk
    private Member member;

    @OneToMany(mappedBy = "order")
    @JoinColumn(name="delivery_id")
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus statusl; // 주문상태 ORDER, CANCEL
}
