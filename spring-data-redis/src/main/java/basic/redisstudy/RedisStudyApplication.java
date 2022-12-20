package basic.redisstudy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RedisStudyApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisStudyApplication.class, args);
	}

}
