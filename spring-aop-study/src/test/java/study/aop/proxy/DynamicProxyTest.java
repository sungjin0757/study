package study.aop.proxy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import org.springframework.boot.test.context.SpringBootTest;

import study.aop.proxy.handler.UpperClassHandler;

import java.lang.reflect.Proxy;

@SpringBootTest
@Slf4j
public class DynamicProxyTest {

//    @Test
//    @DisplayName("Dynamic Proxy Test - UpperClass")
//    void 다이나믹_프록시_테스트_대문자(){
//
//        Hello dynamicProxy= (Hello) Proxy.newProxyInstance(getClass().getClassLoader(),
//                new Class[]{Hello.class}, new UpperClassHandler(new HelloTarget()));
//
//        Assertions.assertThat(dynamicProxy.welcome("h")).isEqualTo("h Welcome".toUpperCase());
//        Assertions.assertThat(dynamicProxy.goodBye("h")).isEqualTo("h Good Bye".toUpperCase());
//    }

    @Test
    @DisplayName("Dynamic Proxt Test - Spring Ver.")
    void 다이나믹_프록시_테스트_스프링_버전(){
        ProxyFactoryBean factoryBean=new ProxyFactoryBean();
        factoryBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut=new NameMatchMethodPointcut();
        pointcut.setMappedNames("welcome","good*");

        factoryBean.addAdvisor(new DefaultPointcutAdvisor(pointcut,new UppercaseAdvice()));

        Hello proxiedHello=(Hello)factoryBean.getObject();
        Assertions.assertThat(proxiedHello.welcome("h")).isEqualTo("h Welcome".toUpperCase());
        Assertions.assertThat(proxiedHello.goodBye("h")).isEqualTo("h Good Bye".toUpperCase());
    }

    @Test
    @DisplayName("Dynamic Proxy Test - Extend Pointcut")
    void 다이나믹_프록시_태스트_확장_포인트컷(){
        NameMatchMethodPointcut pointcut=new NameMatchMethodPointcut(){
            @Override
            public ClassFilter getClassFilter() {
                return new ClassFilter() {
                    @Override
                    public boolean matches(Class<?> clazz) {
                        return clazz.getSimpleName().startsWith("Hello");
                    }
                };
            }
        };
        pointcut.setMappedNames("welcome","good*");

        checkAdviceClass(new HelloTarget(),pointcut,true);
        checkAdviceClass(new HelloBoy(),pointcut,true);
        checkAdviceClass(new SSabal(),pointcut,false);
    }

    private void checkAdviceClass(Object target, Pointcut pointcut, boolean adviced){
        ProxyFactoryBean proxyFactoryBean=new ProxyFactoryBean();
        proxyFactoryBean.setTarget(target);
        proxyFactoryBean.addAdvisor(new DefaultPointcutAdvisor(pointcut,new UppercaseAdvice()));
        Hello proxiedHello=(Hello) proxyFactoryBean.getObject();

        log.info("class = {}",proxiedHello.getClass());

        if(adviced){
            Assertions.assertThat(proxiedHello.welcome("h")).isEqualTo("h welcome".toUpperCase());
            Assertions.assertThat(proxiedHello.goodBye("h")).isEqualTo("h good Bye".toUpperCase());
        }else if(!adviced){
            Assertions.assertThat(proxiedHello.welcome("h")).isEqualTo("h welcome");
            Assertions.assertThat(proxiedHello.goodBye("h")).isEqualTo("h good Bye");
        }
    }

    static class UppercaseAdvice implements MethodInterceptor{
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            return String.valueOf(invocation.proceed()).toUpperCase();
        }
    }

    static interface Hello{
        String welcome(String name);
        String goodBye(String name);
    }

    static class HelloTarget implements Hello{
        @Override
        public String welcome(String name) {
            return name+" welcome";
        }

        @Override
        public String goodBye(String name) {
            return name+" good Bye";
        }
    }

    static class HelloBoy implements Hello{
        @Override
        public String welcome(String name) {
            return name+" welcome";
        }

        @Override
        public String goodBye(String name) {
            return name+" good Bye";
        }
    }

    static class SSabal implements Hello{
        @Override
        public String welcome(String name) {
            return name+" welcome";
        }

        @Override
        public String goodBye(String name) {
            return name+" good Bye";
        }
    }
}
