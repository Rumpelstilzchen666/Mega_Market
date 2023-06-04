package yaroslav;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        // System.out.println("Application start!");
        // System.setOut(new PrintStream(System.getProperty("user.dir") + "\\log.txt", StandardCharsets.UTF_8));
        SpringApplication.run(Application.class, args);
    }
}
