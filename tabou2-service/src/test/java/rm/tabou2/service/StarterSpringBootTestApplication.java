package rm.tabou2.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"rm.tabou2.service", "rm.tabou2.storage.tabou", "rm.tabou2.storage.sig", "rm.tabou2.storage.ddc"})
public class StarterSpringBootTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(StarterSpringBootTestApplication.class, args);
	}
}
