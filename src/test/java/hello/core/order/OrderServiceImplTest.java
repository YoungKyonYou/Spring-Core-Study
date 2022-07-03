package hello.core.order;

import hello.core.discount.FixDiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemoryMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceImplTest {

    //이 Test를 돌리면 NullPointerException이 발생한다. 왜냐면
    //OrderServiceImple.java에서 private 멤버 변수들을 세팅을 안해줘서 그렇다.
    //나는 createOrder()만 테스트하고 싶지만 그 createOrder()를 들어가면 memberRepository와 discountPolicy를 사용하게 된다.
    //그러면 dummy라도 만들어줘서 넣어줘야 하는 것이다. 이것은 누락이 된 것이다.
    //이런 setter Autowired를 만들게 되면 이런 경우를 많이 겪을 것이다.
    //
    @Test
    void createOrder(){
        MemoryMemberRepository memberRepository=new MemoryMemberRepository();
        memberRepository.save(new Member(1L, "name", Grade.VIP));
        OrderServiceImpl orderService = new OrderServiceImpl(new MemoryMemberRepository(), new FixDiscountPolicy());
        Order order = orderService.createOrder(1L, "itemA", 10000);
        Assertions.assertThat(order.getDiscountPrice()).isEqualTo(1000);
    }

}