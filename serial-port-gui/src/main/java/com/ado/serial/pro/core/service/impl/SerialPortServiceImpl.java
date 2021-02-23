package com.ado.serial.pro.core.service.impl;

import com.ado.serial.pro.core.service.SerialPortService;
import enums.SerialPortConfigEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SerialPortServiceImpl implements SerialPortService {


    private static final List<SerialPortConfigEnum> keys = new ArrayList<SerialPortConfigEnum>() {{
        add(SerialPortConfigEnum.PARITY_CHECK);
        add(SerialPortConfigEnum.SERIAL_NUMBER);
        add(SerialPortConfigEnum.BAUD_RATE);
        add(SerialPortConfigEnum.STOP_BITS);
        add(SerialPortConfigEnum.DATA_BITS);
    }};

    public boolean dealData(Map<String, String> map) {
        return false;
    }
}
