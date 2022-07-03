package hello.core.autowired;

import hello.core.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.Nullable;

import java.util.Optional;

public class AutowiredTEst {

    //아래와 같이 구성하면 TestBean를 컴포넌트 스캔하는 것처럼 빈으로 등록해준다.
    @Test
    void AutowiredOption(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);

    }

    static class TestBean{

        //@Autowired의 required 옵션의 기본값은 true이다.
        //역기서 required=true면 오류가 날 것이다 왜냐면 Member가 스프링 빈으로 등록되는 것이 아니기 때문이다.
        //그래서 required false로 해주면 의존관계가 없다면 이 메서드(setNoBean1()) 자체가 호출이 안된다.
        //즉 @Autowired(required=false는 자동 주입할 대상이 없으면 수정자 메서드 자체가 호출이 안된다.
        @Autowired(required=false)
        //스프링 컨테이너에 관리되지 않는 것을 넣었다.(Member)
        public void setNoBean1(Member noBean1){
            System.out.println("noBean1 = " + noBean1);
        }

        @Autowired
        //@Nullable은 자동 주입할 대상이 없으면 null이 입력된다.
        public void setNoBean2(@Nullable Member noBean1){
            System.out.println("noBean2 = " + noBean1);
        }

        @Autowired
        //Optional<>은 자동 주입할 대상이 없으면 Optional.empty가 입력된다.
        public void setNoBean3(Optional<Member> noBean3){
            System.out.println("noBean3 = " + noBean3);
        }
    }
}
