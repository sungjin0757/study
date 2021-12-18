package study.aop.learning;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class LearningTest {

    @Test
    @DisplayName("Reflect Method API Test")
    void 메소드_api_테스트() throws Exception{
        String name="Hong";
        Assertions.assertThat(name.length()).isEqualTo(4);

        Method lengthMethod=String.class.getMethod("length");
        Assertions.assertThat(lengthMethod.invoke(name)).isEqualTo(4);

        Assertions.assertThat(name.charAt(2)).isEqualTo('n');

        Method charAtMethod=String.class.getMethod("charAt", int.class);
        Assertions.assertThat(charAtMethod.invoke(name,2)).isEqualTo('n');
    }

}
