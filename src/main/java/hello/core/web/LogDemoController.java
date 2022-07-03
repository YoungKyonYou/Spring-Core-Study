package hello.core.web;

import hello.core.common.MyLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * ObjectProvider 덕분에 ObjectProvider.getObject()를 호출하는 시점까지 request scope 빈의 생성을 지연할 수 있다
 * ObjectProvider.getObject()를 호출하는 시점에는 HTTP 요청이 진행중이므로 request scope 빈의 생성이 정상처리된다.
 * ObjectProvider.getObejct()를 LogDemoController, LogDemoService에서 각각 한번씩 따로 호출해도 같은
 * HTTP 요청이면 같은 스프링 빈이 반환된다.
 */
@Controller
@RequiredArgsConstructor
public class LogDemoController {
    private final LogDemoService logDemoService;
    private final MyLogger myLogger;
    //이렇게 하면 MyLogger를 주입받는 것이 아니라 Dependency Lookup를 할 수 있는 얘가 주입이 된다.
   // private final ObjectProvider<MyLogger> myLoggerObjectProvider;

    @RequestMapping("log-demo")
    //반환해주는 String 값으로 바로 응답값을 보낼 수 있게 해주는 어노테이션
    @ResponseBody
    public String logDemo(HttpServletRequest request) throws InterruptedException {
      //  MyLogger myLogger = myLoggerObjectProvider.getObject();
        String requestURL = request.getRequestURI().toString();
        myLogger.setRequestURL(requestURL);

        myLogger.log("controller test");
        Thread.sleep(1000);
        logDemoService.logic("testId");
        return "OK";
    }
}
