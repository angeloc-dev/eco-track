package unito.tweb.ecotrackspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class EcoTrackSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcoTrackSpringApplication.class, args);
    }

}
