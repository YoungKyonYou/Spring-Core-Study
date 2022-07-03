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
