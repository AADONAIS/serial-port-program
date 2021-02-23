package com.pss.serial.port.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class SerialportProgramApplication {

    public static void main(String[] args) {
        SpringApplication.run(SerialportProgramApplication.class, args);
        log.info("[{}] : [{}],args:[{}]" , "系统启动模块" , "串口监听程序启动....",args);
    }

}
