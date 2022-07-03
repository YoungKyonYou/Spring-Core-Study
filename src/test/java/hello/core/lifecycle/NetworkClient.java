package hello.core.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
//초기화, 소멸 인터페이스 단점
/**
 * 이 인터페이스는 스프링 전용 인터페이스이다. 해당 코드가 스프링 전용 인터페이스에 의존한다.
 * 초기화, 소멸 메서드의 이름을 변경할 수 없다.
 * 내가 코드를 고칠 수 없는 외부 라이브러리에 적용할 수 없다.
 * 인터페이스를 사용하는 초기화, 종료방법은 스프링 초창기에 나온 방법들이고, 지금은 더 나은 방법이 있어서 거의 사용하지 않는다.
 */
public class NetworkClient /*implements InitializingBean, DisposableBean*/ {

    private String url;

    public NetworkClient(){
        System.out.println("생성자 호출, url = " + url);
//        connect();
//        call("초기화 연결 메시지");
    }

    public void setUrl(String url){
        this.url=url;
    }

    //서비스 시작시 호출
    public void connect(){
        System.out.println("connect: " + url);
    }

    public void call(String message){
        System.out.println("call: "+url+" message = "+message);
    }

    //서비스 종료시 호출
    public void disconnect(){
        System.out.println("close "+url);
    }


    public void init() {
        System.out.println("NetworkClient.afterPropertiesSet");
        connect();
        call("초기화 연결 메시지");
    }
    public void close() {
        System.out.println("NetworkClient.destroy");
        disconnect();
    }


/*    //스프링이 의존관계 주입이 끝나면 호출해주는 함수
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("NetworkClient.afterPropertiesSet");
        connect();
        call("초기화 연결 메시지");
    }

    //빈이 종료될 때 호출됨
    @Override
    public void destroy() throws Exception {
        System.out.println("NetworkClient.destroy");
        disconnect();
    }*/
}
