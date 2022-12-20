package study.aop.proxy;

public class HelloTarget implements Hello{
    @Override
    public String welcome(String name) {
        return name+" Welcome";
    }

    @Override
    public String goodBye(String name) {
        return name+" Good Bye";
    }
}
