package study.aop.proxy.pointcut;

public interface TargetInterface {
    void hello();
    void hello(String text);
    int minus(int a, int b) throws RuntimeException;
    int plus(int a,int b);
}
