package site.concertseat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ConcertseatApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConcertseatApplication.class, args);
	}

}
