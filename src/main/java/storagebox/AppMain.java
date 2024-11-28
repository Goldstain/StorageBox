package storagebox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "storagebox")
public class AppMain {

    public static void main(String[] args) {
        SpringApplication.run(AppMain.class, args);
    }
}