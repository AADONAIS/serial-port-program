package com.pss.serial.port.core.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pss.serial.port.core.utils.SerialportUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import purejavacomm.CommPortIdentifier;
import purejavacomm.PortInUseException;
import purejavacomm.SerialPort;
import purejavacomm.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * 串口数据测试http接口类
 * @author ADONAIS
 */
@RestController
@RequestMapping(value = "/serial/port")
public class SerialPortController {

    private static final Logger log = LoggerFactory.getLogger(SerialPortController.class) ;

    @RequestMapping(value = "/getData" , method = RequestMethod.GET)
    public void getData() {
//        SerialportUtil.
        // 串口打开后的串口对象
        SerialPort serialPort = null;
        // 串口的输入流对象
        InputStream inputStream = null;

        Enumeration<?> en = CommPortIdentifier.getPortIdentifiers();
        CommPortIdentifier port = null;

        while (en.hasMoreElements()) {
            CommPortIdentifier portId = (CommPortIdentifier) en.nextElement();
            // 如果端口类型是串口, 则打印信息
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                System.out.println(portId.getName());
                // 选取我们需要的串口端口
                if (portId.getName().equals("COM1")) {
                    port = portId;
                }
            }
        }

        if (port == null) {
            System.out.println("没有获取到需要的串口");
            log.info("没有获取到需要的串口");
        }

        try {
            // 打开串口
            serialPort = (SerialPort) port.open("Main", 2000);
            // 获取串口的输入流对象
            inputStream = serialPort.getInputStream();
        } catch (PortInUseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        } catch (UnsupportedCommOperationException e) {
            e.printStackTrace();
        }

        byte[] readB = new byte[21];
        int nBytes = 0;
        while(true) {
            try {
                while (inputStream.available() > 0) {
                    nBytes = inputStream.read(readB);
                }
                // 将读出的字符数组数据，直接转换成十六进制。
                //StringToHex.printHexString(readB);
                System.out.println(new String(readB));
                log.info(new String(readB));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @RequestMapping(value = "/getSerialList" , method = RequestMethod.GET)
    public String getSerialList() {
        //获得当前所有可用串口
        Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();

        ArrayList<String> portNameList = new ArrayList<>();

        if(CollectionUtil.isEmpty(portList)) {
            return "没有串口可用" ;
        }
        //将可用串口名添加到List并返回该List
        while (portList.hasMoreElements()) {
            String portName = portList.nextElement().getName();
            portNameList.add(portName);
        }
        return JSONObject.toJSONString(portList);
    }
}
