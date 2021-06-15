package com.anbang.qipai.huaibinmajiang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class QipaiHuaibinmajiangApplication {

    public static void main(String[] args) {
        SpringApplication.run(QipaiHuaibinmajiangApplication.class, args);
    }

}