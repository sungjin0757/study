package study.template.exercise;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class SumTest {

    CalculatorV3 cal;
    String path;

    @BeforeEach
    void setUp(){
        cal=new CalculatorV3();
        path=getClass().getResource("").getPath()+"numbers.txt";
    }

    @Test
    void 더하기_테스트() throws IOException {
        CalculatorV2 cal=new CalculatorV2();
        int sum=cal.sum(path);
        Assertions.assertThat(sum).isEqualTo(10);
    }
}
