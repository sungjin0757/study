package basic.fileloadapi;

import basic.fileloadapi.common.property.FileUploadProperty;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.EntityManager;

@EnableJpaAuditing
@EnableConfigurationProperties({FileUploadProperty.class})
@SpringBootApplication
public class FileLoadApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileLoadApiApplication.class, args);
	}

	@Bean
	public JPAQueryFactory jpaQueryFactory(EntityManager em) {
		return new JPAQueryFactory(em);
	}

}
