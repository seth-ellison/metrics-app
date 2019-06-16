package metrics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Origin point for starting up our Spring Boot application.
 * Top-level configuration can be specified here using @annotations.
 * 
 * For more specific configuration, create new @Configuration classes
 * in the metrics.config.* package.
 * 
 * @author Seth
 *
 */
@SpringBootApplication
public class MetricsAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MetricsAppApplication.class, args);
	}
}
