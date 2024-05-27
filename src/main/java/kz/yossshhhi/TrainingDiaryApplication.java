package kz.yossshhhi;

import kz.yossshhhi.starter.audit.aop.annotation.EnableAudit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAudit
@SpringBootApplication
public class TrainingDiaryApplication {
    public static void main(String[] args) {
        SpringApplication.run(TrainingDiaryApplication.class, args);
    }
}
