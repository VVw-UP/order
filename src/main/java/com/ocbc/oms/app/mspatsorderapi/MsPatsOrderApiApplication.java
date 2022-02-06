package com.ocbc.oms.app.mspatsorderapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.TimeZone;

@EnableKafka
@SpringBootApplication
@ComponentScan(basePackages = {"com.ocbc.oms.app"})
@MapperScan("com.ocbc.oms.app.repository")
@EnableTransactionManagement
@EnableScheduling
public class MsPatsOrderApiApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Singapore"));
        SpringApplication.run(MsPatsOrderApiApplication.class, args);
    }

}
