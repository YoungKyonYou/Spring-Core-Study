package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;
import org.springframework.stereotype.Component;

//이 @Component를 주석 처리한 이유는 빈으로 DiscountPolicy를 등록할 때 사용할 정책을 RateDiscountPolicy로 하기 위함이다
//현재 RateDiscountPolicy.java에 @Component가 되어 있다. 현재 이 FixDiscountPolicy에 @Component의 주석을 풀면 어떤 빈을 등록할지
//몰라서 오류가 난다.
//@Component
public class FixDiscountPolicy implements DiscountPolicy{
    private int discountFixAmount=1000;
    @Override
    public int discount(Member member, int price) {
        if(member.getGrade()== Grade.VIP){
            return discountFixAmount;
        }else {
            return 0;
        }
    }
}
