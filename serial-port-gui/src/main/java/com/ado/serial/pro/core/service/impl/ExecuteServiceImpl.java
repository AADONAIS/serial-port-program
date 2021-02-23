package com.ado.serial.pro.core.service.impl;

import com.ado.serial.pro.core.service.ExecuteService;
import enums.SerialPortConfigEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExecuteServiceImpl implements ExecuteService {

    public static void main(String[] args) {
        HashMap<String, String> map = new HashMap<String, String>();
        ExecuteService executeService = new ExecuteServiceImpl();
        executeService.dealData(map);
    }

    private static final List<SerialPortConfigEnum> keys = new ArrayList<SerialPortConfigEnum>() {{
        add(SerialPortConfigEnum.PARITY_CHECK);
        add(SerialPortConfigEnum.SERIAL_NUMBER);
        add(SerialPortConfigEnum.BAUD_RATE);
        add(SerialPortConfigEnum.STOP_BITS);
        add(SerialPortConfigEnum.DATA_BITS);
    }};

    private static final String commoandCons = "java -jar .\\classes\\serial-port-core-1.0.jar";


    public boolean dealData(Map<String, String> map) {
        CommandServiceImpl commandServiceImpl = new CommandServiceImpl();
        String params = spliceParams(map);
        String command = commoandCons+params;
        String[] cmds = { "cmd", "/c", command };
        commandServiceImpl.exec(cmds);
        return true;
    }


    private String spliceParams(Map<String,String> map){
        String out="";

        for (SerialPortConfigEnum key : keys) {
            if (map.containsKey(key.getCode())){
                String value = map.get(key);
                out+=" --"+key+"="+value+" ";
            }
        }

        return out;

    }




}
