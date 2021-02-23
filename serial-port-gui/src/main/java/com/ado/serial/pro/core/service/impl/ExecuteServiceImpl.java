package com.ado.serial.pro.core.service.impl;

import com.ado.serial.pro.core.service.ExecuteService;
import com.ado.serial.pro.core.ui.HomeUI;
import enums.SerialPortConfigEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExecuteServiceImpl implements ExecuteService {

    private HomeUI homeUI;
    public ExecuteServiceImpl(HomeUI homeUI) {
        this.homeUI=homeUI;
    }

//    public static void main(String[] args) {
//        HashMap<String, String> map = new HashMap<String, String>();
//        ExecuteService executeService = new ExecuteServiceImpl();
//        executeService.dealData(map);
//    }

    private static final List<SerialPortConfigEnum> keys = new ArrayList<SerialPortConfigEnum>() {{
        add(SerialPortConfigEnum.PARITY_CHECK);
        add(SerialPortConfigEnum.SERIAL_NUMBER);
        add(SerialPortConfigEnum.BAUD_RATE);
        add(SerialPortConfigEnum.STOP_BITS);
        add(SerialPortConfigEnum.DATA_BITS);
    }};

    private static final String commoandCons = "java -jar .\\classes\\serial-port-core-1.0.jar";


    public boolean dealData(Map<String, String> map) {
        CommandServiceImpl commandServiceImpl = new CommandServiceImpl(homeUI);
        String params = spliceParams(map);
        String command = commoandCons+params;
        System.out.println("#####");
        System.out.println(command);
        System.out.println("#####");
        String[] cmds = { "cmd", "/c", command };
        commandServiceImpl.exec(cmds);
        return true;
    }


    private String spliceParams(Map<String,String> map){
        String out="";

        for (SerialPortConfigEnum key : keys) {
            if (map.containsKey(key.getCode())){
                String value = map.get(key.getCode());
                if (value.equals("无")){
                    continue;
                }

                out+=" --"+key.getCode()+"="+value+" ";
            }
        }

        return out;

    }




}
