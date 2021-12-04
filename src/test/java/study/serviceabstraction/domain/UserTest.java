package study.serviceabstraction.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import study.serviceabstraction.dao.Level;

public class UserTest {

    User user;

    @BeforeEach
    void setUp(){
        user=new User();
    }

    @Test
    @DisplayName("Business Logic Test")
    void 도메인_로직_테스트(){
        Level[] values = Level.values();

        for(Level level:values){
            user.updateLevel(level);
            if(level.getNext()==null){
                Assertions.assertThrows(IllegalStateException.class,()->{
                   user.upgradeLevel();
                });
            }else if(level.getNext()!=null){
                user.upgradeLevel();
                org.assertj.core.api.Assertions.assertThat(user.getLevel()).isEqualTo(level.getNext());
            }
        }
    }
}
