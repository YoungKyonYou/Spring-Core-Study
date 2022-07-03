package hello.core.common;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.UUID;

@Component
//@Scope(value="request")
//프록시로 만든다는 것이다 현재 이클래스를 타겟으로 한다.
/**
 * 적용 대상이 인터페이스가 아닌 클래스면 'TARGET_CLASS'를 선택하고
 * 적용대상이 인터페이스면 'INTERFACES'를 선택한다.
 * 이렇게 하면 MyLogger의 가짜 프록시 클래스를 만들어두고 HTTP request와 상관 없이 가짜 프록시 클래스를 다른 빈에 미리 주입해 둘 수 있다.
 */
/**
 * 스프링이 조작해서 만든 얘가 스프링 빈으로 등록되게 된다(위에 찌기는 거 참고) 얘는 진짜 MyLogger가 아니라 껍데기이다.
 * 진짜 기능을 호출하는 시점에 진짜 Provider가 동작했던 것처럼 동작하게 되는 것이다. CGLIB라는 라이브러리로 내 클래스를
 * 상속 받은 가짜 프록시 객체를 만들어서 주입한다. 결과를 확인해보면 우리가 등록한 순수한 MyLogger 클래스가 아니라
 * MyLogger$$EnhancerBySpringCGLIB이라는 클래스로 만들어진 객체가 대신 등록되어 지는 것을 확인할 수 있다.
 * 그리고 스프링 컨테이너에 "myLogger"라는 이름으로 진짜 대신에 이 가짜 프록시 객체를 등록한다.
 * ac.getBean("myLogger", MyLogger.class)로 조회해도 프록시 객체가 조회되는 것을 확인할 수 있다.
 *  그래서 의존관계 주입도 이 가짜 프록시 객체가 주입된다.
 *  가짜 프록시 객체는 요청이 오면 그때 내부에서 진짜 빈을 요청하는 위임 로직이 들어가 있다.
 *  이 가짜 프록시 빈은 내부에 실제 MyLogger 를 찾는 방법을 가지고 있다.
 *  클라이언트가 myLogger.logic()을 호출하면 사실은 가짜 프록시 객체의 메서드를 호출한 것이다
 *  가짜 프록시 객체는 request 스코프의 진짜 myLogger.logic()를 호출한다
 *  가짜 프록시 객체는 원본 클래스를 상속 받아서 만들어졌기 때문에 이 객체를 사용하는 클라이언트 입장에서는 사실
 *  원본인지 아닌지도 모르게 동일하게 사용할 수 있다(다형성)
 *
 *  동작원리
 *  CGLIB라는 라이브러리로 내 클래스를 상속 받은 가짜 프록시 객체를 만들어서 주입한다.
 *  이 가짜 프록시 객체는 실제 요청이 오면 그때 내부에서 실제 빈을 요청하는 위임 로직이 들어있다
 *  가짜 프록시 객체는 실제 request scope와는 관계가 없다. 그냥 가짜이고 내부에 단순한 위임 로직만 있고 싱글톤처럼 동작한다.
 *
 *  특징 정리
 *  프로시 객체 덕분에 클라이언트는 마치 싱글톤 빈을 사용하듯이 편리하게 request scope를 사용할 수 있다.
 *  사실 Provider를 사용하든 프록시를 사용하든 핵심 아이디어는 진짜 객체 조회를 꼭 필요한 시점까지 지연처리 한다는 점이다.
 *  단지 애노테이션 설정 변경만으로 원본 객체를 포록시 객체로 대체할 수 있다. 이것이 바로 다형성과 DI컨테이너가 가진
 *  큰 강점이다. 꼭 웹 스코프가 아니어도 프록시는 사용할 수 있다.
 *
 *  주의점
 *  마치 싱글톤을 사용하는 것 같지만 다르게 동작하기 때문에 결국 주의해서 사용해야 한다.
 *  이런 특별한 scope는 꼭 필요한 곳에만 최소화해서 사용하자, 무분별하게 사용하면 유지보수하기 어려워진다.
 */
@Scope(value="request", proxyMode= ScopedProxyMode.TARGET_CLASS)
public class MyLogger {
    private String uuid;
    private String requestURL;
   // requestURL은 이 빈이 생성되는 시점에는 알 수 없으므로외부에서 setter로 입력받는다.
    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public void log(String message){
        System.out.println("["+uuid+"]"+"["+requestURL+"]"+message);

    }

    //request가 서버에 들어왔을때 호출

    /**
     * 이 빈이 생성되는 시점에 자동으로 @PostConstruct 초기화 메서드를 사용해서 uuid를 생성해서 저장해둔다.
     * 이 빈은 HTTP 요청 당 하나씩 생성되므로 uuid를 저장해두면 다른 HTTP 요청과 구분할 수 있다.
     */
    @PostConstruct
    public void init(){
        uuid=UUID.randomUUID().toString();
        System.out.println("["+uuid+"] request scope bean create:"+this);

    }
    //request가 서버에서 빠져나갈 때 호출

    /**
     *이 빈이 소멸되는 시점에 @PreDestroy를 사용해서 종료 메시지를 남긴다.
     */
    @PreDestroy
    public void close(){
        System.out.println("["+uuid+"] request scope bean close:"+this);

    }
}
