package lol.koblizek.notecz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@SpringBootApplication
public class NoteczApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoteczApplication.class, args);
    }

    @Bean
    public MethodValidationPostProcessor validator() {
        return new MethodValidationPostProcessor();
    }
}
