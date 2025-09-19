package edu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {
        "security.config",
        "security.jwt",
        "security.principal",
        "repo",
        "model.entity"
})
public class TplusJv250321Md06ProjectApplication {


    public static void main(String[] args) {
        SpringApplication.run(TplusJv250321Md06ProjectApplication.class, args);
    }

}
