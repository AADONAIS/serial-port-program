package com.pss.serial.port.core;

import com.pss.serial.port.core.utils.PortUtils;
import gnu.io.CommPortIdentifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Enumeration;

@Slf4j
@SpringBootApplication
public class SerialportProgramApplication {

    public static void main(String[] args) {
//        Runnable portUtils = new PortUtils();
//        portUtils.run();


        SpringApplication.run(SerialportProgramApplication.class, args);
        log.info("[{}] : [{}],args:[{}]" , "系统启动模块" , "串口监听程序启动....",args);
    }



}
