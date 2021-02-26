package com.pss.serial.port.core.utils;

import com.pss.serial.port.core.exception.BusinessException;
import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class SerialportUtil {

    private static SerialportUtil serialPortUtil = null;

    static {
        //在该类被ClassLoader加载时就初始化一个SerialTool对象
        if (serialPortUtil == null) {
            serialPortUtil = new SerialportUtil();
        }
    }

    //私有化SerialTool类的构造方法，不允许其他类生成SerialTool对象
    private SerialportUtil() {
    }

    /**
     * 获取提供服务的SerialTool对象
     *
     * @return serialPortUtil
     */
    public static SerialportUtil getSerialPortUtil() {
        if (serialPortUtil == null) {
            serialPortUtil = new SerialportUtil();
        }
        return serialPortUtil;
    }


    /**
     * 查找所有可用端口
     *
     * @return 可用端口名称列表
     */
    public static final ArrayList<String> findPort() {
        //获得当前所有可用串口
        Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();

        ArrayList<String> portNameList = new ArrayList<>();

        //将可用串口名添加到List并返回该List
        while (portList.hasMoreElements()) {
            String portName = portList.nextElement().getName();
            portNameList.add(portName);
        }

        return portNameList;
    }

    /**
     * 打开串口
     */
    public static final SerialPort openPort(String portName, int baudrate, int databits, int parity, int stopbits)  {

        try {
            //通过端口名识别端口
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);

            //打开端口，并给端口名字和一个timeout（打开操作的超时时间）
            CommPort commPort = portIdentifier.open(portName, 2000);

            //判断是不是串口
            if (commPort instanceof SerialPort) {

                SerialPort serialPort = (SerialPort) commPort;
                try {
                    //设置一下串口的波特率等参数
                    serialPort.setSerialPortParams(baudrate, databits, stopbits, parity);
                } catch (UnsupportedCommOperationException e) {
                    throw new BusinessException("500" , "异常") ;
                }

                //System.out.println("Open " + portName + " sucessfully !");
                return serialPort;
            } else {
                //不是串口
                throw new BusinessException("500" , "不是串口") ;
            }
        } catch (NoSuchPortException e1) {
            throw new BusinessException("500" , "没有这个串口") ;
        } catch (PortInUseException e2) {
            throw new BusinessException("500" , "串口正在被占用") ;
        }
    }

    /**
     * 关闭串口
     *
     * @param serialPort 待关闭的串口对象
     */
    public static void closePort(SerialPort serialPort) {
        if (serialPort != null) {
            serialPort.close();
            serialPort = null;
        }
    }

    /**
     * 往串口发送数据
     */
    public static void sendToPort(SerialPort serialPort, byte[] order) {
        OutputStream out = null;
        try {
            out = serialPort.getOutputStream();
            out.write(order);
            out.flush();
        } catch (IOException e) {
            throw new BusinessException("500" , "发送串口数据失败") ;
        } finally {
            try {
                if (out != null) {
                    out.close();
                    out = null;
                }
            } catch (IOException e) {
                throw new BusinessException("500" , "串口数据流关闭异常") ;
            }
        }
    }

    /**
     * 从串口读取数据
     *
     */
    public static byte[] readFromPort(SerialPort serialPort) {

        InputStream in = null;
        byte[] bytes = null;

        try {
            in = serialPort.getInputStream();
            int bufflenth = in.available();        //获取buffer里的数据长度

            while (bufflenth != 0) {
                bytes = new byte[bufflenth];    //初始化byte数组为buffer中数据的长度
                in.read(bytes);
                bufflenth = in.available();
            }
        } catch (IOException e) {
        } finally {
            try {
                if (in != null) {
                    in.close();
                    in = null;
                }
            } catch (IOException e) {
            }
        }

        return bytes;

    }

    /**
     * 添加监听器
     */
    public static void addListener(SerialPort port, SerialPortEventListener listener){

        try {
            //给串口添加监听器
            port.addEventListener(listener);
            //设置当有数据到达时唤醒监听接收线程
            port.notifyOnDataAvailable(true);
            //设置当通信中断时唤醒中断线程
            port.notifyOnBreakInterrupt(true);
        } catch (TooManyListenersException e) {
        }
    }

    /**
     * 删除监听器
     *
     * @param port     串口对象
     * @param listener 串口监听器
     * @throws
     */
    public static void removeListener(SerialPort port, SerialPortEventListener listener) {
        //删除串口监听器
        port.removeEventListener();
    }

}
