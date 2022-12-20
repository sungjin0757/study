package study.template.exercise;

import java.io.BufferedReader;
import java.io.IOException;

public interface ExerciseCallback {
    int doSomething(BufferedReader br) throws IOException;
}
