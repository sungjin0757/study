package chapter2;

/**
 * 자바로 이뤄진 클래스
 * 필드가 둘 이상으로 늘어나면 생성자의 파라미터 대입수도 늘어남. 반면 코틀린은 훨씬 더 적은 코드로 작성 가능
 */
public class PersonJava {
    private final String name;

    public PersonJava(String name) {
        this.name = name;
    }
}
