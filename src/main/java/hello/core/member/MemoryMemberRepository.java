package hello.core.member;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

//컴포넌트 스캔을 하기 위해서 필요 , AutoAppConfig.java 주석 참고
@Component
public class MemoryMemberRepository implements MemberRepository{
    private static Map<Long, Member> store=new HashMap<>();

    @Override
    public void save(Member member) {
        store.put(member.getId(),member);
    }

    @Override
    public Member findById(Long memberId) {
        return store.get(memberId);
    }
}
