package com.pss.serial.port.core.utils;


import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TooManyListenersException;

/**
 * PortUtils
 *
 * @author Leon
 * @version 1.0
 * @date 2021/2/24 18:46
 **/
public class PortUtils implements Runnable {

    @Override
    public void run() {

        ArrayList<String> ports = findPorts();
        for (String port : ports) {
            System.out.println(port);
        }
        SerialPort serialPort = openPort("COM1", 9600);
        while (true){
            String s = readFromPort(serialPort);
            System.out.println(s);
        }

    }



    /**
     * 查找所有可用端口
     *
     * @return 可用端口名称列表
     */
    public static ArrayList<String> findPorts() {
        // 获得当前所有可用串口
        Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();
        ArrayList<String> portNameList = new ArrayList<String>();
        // 将可用串口名添加到List并返回该List
        while (portList.hasMoreElements()) {
            String portName = portList.nextElement().getName();
            portNameList.add(portName);
        }
        return portNameList;
    }

    /**
     * 打开串口
     *
     * @param portName 端口名称
     * @param baudrate 波特率
     * @return 串口对象
     */
    public static SerialPort openPort(String portName, int baudrate) {
        try {
            // 通过端口名识别端口
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
            // 打开端口，并给端口名字和一个timeout（打开操作的超时时间）
            CommPort commPort = portIdentifier.open(portName, 2000);
            // 判断是不是串口
            if (commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                try {
                    // 设置一下串口的波特率等参数
                    // 数据位：8
                    // 停止位：1
                    // 校验位：None
                    serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                } catch (UnsupportedCommOperationException e) {
                    e.printStackTrace();
                }
                return serialPort;
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭串口
     * <p>
     * 待关闭的串口对象
     */
    public static void closePort(SerialPort serialPort) {
        if (serialPort != null) {
            serialPort.close();
        }
    }


    /**
     * 从串口读取数据
     *
     * @param serialPort 当前已建立连接的SerialPort对象
     * @return 读取到的数据
     */
    public static String readFromPort(SerialPort serialPort) {
        InputStream inputStream = null;
        byte[] readAarray = new byte[1];
        StringBuilder stringBuilder = new StringBuilder();

        try {

            inputStream = serialPort.getInputStream();
            while (true) {
                inputStream.read(readAarray);
                stringBuilder.append(new String(readAarray));
                if (stringBuilder.toString().contains("\r\n")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                    inputStream = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 添加监听器
     * <p>
     * 串口对象
     *
     * @param listener 串口存在有效数据监听
     */
    public static void addListener(SerialPort serialPort, DataAvailableListener listener) {
        try {
            // 给串口添加监听器
            serialPort.addEventListener(new SerialPortListener(listener));
            // 设置当有数据到达时唤醒监听接收线程
            serialPort.notifyOnDataAvailable(true);
            // 设置当通信中断时唤醒中断线程
            serialPort.notifyOnBreakInterrupt(true);
        } catch (TooManyListenersException e) {
            e.printStackTrace();
        }
    }

    /**
     * 串口监听
     */
    public static class SerialPortListener implements SerialPortEventListener {

        private DataAvailableListener mDataAvailableListener;

        public SerialPortListener(DataAvailableListener mDataAvailableListener) {
            this.mDataAvailableListener = mDataAvailableListener;
        }

        public void serialEvent(SerialPortEvent serialPortEvent) {
            switch (serialPortEvent.getEventType()) {
                case SerialPortEvent.DATA_AVAILABLE: // 1.串口存在有效数据
                    if (mDataAvailableListener != null) {
                        mDataAvailableListener.dataAvailable();
                    }
                    break;

                case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2.输出缓冲区已清空
                    break;

                case SerialPortEvent.CTS: // 3.清除待发送数据
                    break;

                case SerialPortEvent.DSR: // 4.待发送数据准备好了
                    break;

                case SerialPortEvent.RI: // 5.振铃指示
                    break;

                case SerialPortEvent.CD: // 6.载波检测
                    break;

                case SerialPortEvent.OE: // 7.溢位（溢出）错误
                    break;

                case SerialPortEvent.PE: // 8.奇偶校验错误
                    break;

                case SerialPortEvent.FE: // 9.帧错误
                    break;

                case SerialPortEvent.BI: // 10.通讯中断
//                    ShowUtils.errorMessage("与串口设备通讯中断");
                    break;

                default:
                    break;
            }
        }
    }

    /**
     * 串口存在有效数据监听
     */
    public interface DataAvailableListener {
        /**
         * 串口存在有效数据
         */
        void dataAvailable();
    }
}
