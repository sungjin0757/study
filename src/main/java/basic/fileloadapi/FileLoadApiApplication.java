package basic.fileloadapi;

import basic.fileloadapi.common.property.FileUploadProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EnableConfigurationProperties({FileUploadProperty.class})
@SpringBootApplication
public class FileLoadApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileLoadApiApplication.class, args);
	}

}
