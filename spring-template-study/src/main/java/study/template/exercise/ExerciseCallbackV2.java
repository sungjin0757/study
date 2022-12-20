package study.template.exercise;

import java.io.BufferedReader;
import java.io.IOException;

public interface ExerciseCallbackV2<T> {
    T doSomething(int medium,T res) throws IOException;
}
