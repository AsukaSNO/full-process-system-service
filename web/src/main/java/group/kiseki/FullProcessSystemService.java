package group.kiseki;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 全流程系统服务应用程序主类
 *
 * @author Yan
 * @version 1.0.0
 */
@SpringBootApplication
@EnableScheduling
public class FullProcessSystemService {

    public static void main(String[] args) {
        SpringApplication.run(FullProcessSystemService.class, args);
    }
}
