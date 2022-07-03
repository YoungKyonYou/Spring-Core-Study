package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;
import org.springframework.stereotype.Component;
//FixDiscountPolicy.java의 주석 확인하기
//컴포넌트 스캔을 하기 위해서 필요 , AutoAppConfig.java 주석 참고
@Component
public class RateDiscountPolicy implements DiscountPolicy{
    private int discountPercent=10;

    @Override
    public int discount(Member member, int price){
        if(member.getGrade()== Grade.VIP){
            return price*discountPercent/100;
        }else{
            return 0;
        }
    }
}
