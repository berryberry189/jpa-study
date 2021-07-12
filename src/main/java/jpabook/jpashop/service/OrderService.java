package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepositoryOld;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final MemberRepositoryOld memberRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    // 주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count){
        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        // 주문 상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);
        /**   ↓
         * Order.java
         *     @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
         *     private List<OrderItem> orderItems = new ArrayList<>();
         *
         *     @OneToOne(fetch = FetchType.LAZY,  cascade = CascadeType.ALL)
         *     @JoinColumn(name="delivery_id")
         *     private Delivery delivery;
         *
         *     => cascade = CascadeType.ALL로 선언되어 있으므로
         *        Order가 persist될때  OrderItem과 Delivery도 persist된다
         *        but, order뿐만이 아니라 다른곳에서도 OrderItem과 Delivery를 중요하게 사용한다면
         *        cascade를 사용하면 안된다.
         */

        return order.getId();
    }

    // 주문 취소
    @Transactional
    public void cancelOrder(Long orderId){

        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);

        // 주문 취소
        order.cancle();
    }

    // 주문 검색
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }

}
