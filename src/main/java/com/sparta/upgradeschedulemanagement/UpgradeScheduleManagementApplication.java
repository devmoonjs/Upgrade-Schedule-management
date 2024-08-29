package com.sparta.upgradeschedulemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class UpgradeScheduleManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(UpgradeScheduleManagementApplication.class, args);
    }

}
