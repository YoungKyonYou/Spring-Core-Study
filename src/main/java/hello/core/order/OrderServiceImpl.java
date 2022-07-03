package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;

@Component
//이제 생성자가 필요가 없어진다. 이 어노테이션으로 인해 생성자를 없애도 된다.
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    //final를 이렇게 선언해주면 좋은 것은 불변이다라는 것이고 생성자에서만 세팅할 수 있게 된다는 것이다.
    //즉, 생성자에서만 값을 넣어줄 수 있다.
    /**
     * 생성자 주입 방식을 선택하는 이유는 여러가지가 있지만 프레임워크에 의존하지 않고 순수한 자바 언어의 특징을 잘 살리는 방법이기도 하다.
     * 기본으로 생성자 주입을 사용하고 필수 값이 아닌 경우에는 setter 주입 방식을 옵션으로 부여하면 된다. 생성자 주입과 setter 주입을 동시에 사용할 수 있다.
     * 항상 생성자 주입을 선택해라!!!!!!!!!!!! 그리고 가끔 옵션이 필요하면 수정자 주입을 선택해라. 필드 주입은 사용하지 않는게 좋다.
     */
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;
//      private MemberRepository memberRepository;
//      private DiscountPolicy discountPolicy;

//    //setter 의존성 주입
//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }
//
//    //setter 의존성 주입
//    @Autowired
//    public void setDiscountPolicy(DiscountPolicy discountPolicy){
//        this.discountPolicy=discountPolicy;
//    }



    /**
     * MemberServiceImpl.java의 생성자 주석 확인
     *
     */
    /**
     * @Autowired에는 특별한 기능이 있는데 지금 DiscountPolicy에 어떤 구체적인 클래스가 대입될 지 모른다
     * 지금 FixDiscount와 RateDiscount가 있는데 이중 RateDiscount가 선택되는 이유는 FixDiscountPolicy.java에서
     * 내가 @Component를 주석 처리하고 RateDiscountPolicy에만 주석을 풀어놨기 때문에 RateDiscountPolicy를 쓰는지 안다.
     * 만약 두개다 @Component가 선언되어 있으면 문제가 발생하는데 이때 @Autowired는 빈 2개 문제가 생길 때
     * 필드 이름으로 매칭 시켜주는 기능이 있다 그래서 2개가 빈이 등록되어 있을 때 만약
     * this.discountPolicy = fixDiscountPolicy 라고 해주면 FixedDiscountPolicy가 매핑이 된다!
     * 이렇게 문제가 발생했을 땐 이런 방법도 있고 @Qualifier를 사용하는 방법가 @Primary를 사용하는 방법이 있다 이는 강의를 참고한다
     * 강의: 의존관계 자동 주입 - @Autowired 필드 명, @Qualifier, @Primary 편
     */
//    //@Autowired 여기서 생략 가능.
//    @Autowired
//    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
//        this.memberRepository = memberRepository;
//        this.discountPolicy = discountPolicy;
////        System.out.println("memberRepository = " + memberRepository);
////        System.out.println("discountPolicy = " + discountPolicy);
//
//    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member=memberRepository.findById(memberId);
        int discountPrice=discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    //테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
