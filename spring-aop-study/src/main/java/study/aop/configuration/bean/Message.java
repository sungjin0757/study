package study.aop.configuration.bean;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Message {
    private final String text;

    public static Message newMessage(String text){
        return new Message(text );
    }
}
