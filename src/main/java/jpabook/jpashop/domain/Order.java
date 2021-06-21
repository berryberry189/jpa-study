package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue
    @Column(name="order_id")
    private Long id;

    // 연관 관계의 주인
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id") // fk
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // order에 저장하면 orderItems에도 저장이 되게
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY,  cascade = CascadeType.ALL)
    @JoinColumn(name="delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 ORDER, CANCEL

    //== 연관관계 메서드 ==//
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public static void main(String[] args){
        Member member = new Member();
        Order order = new Order();

        member.getOrders().add(order);
        order.setMember(member);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // 생성 메서드 -  Order를 생성할때 무조건 호출
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // 비지니스 로직

    // 주문 취소
    public void cancle(){
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송 완료된 상품은 취소가 불가 합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for(OrderItem orderItem : orderItems){
            // 재고 원복
            orderItem.cancel();
        }
    }

    // 전체 주문 가격 조회
    public int getTotalPrice(){
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
        // == return orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
    }



}
