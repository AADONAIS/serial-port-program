//package com.pss.serial.port.core.websocket;
//
//import com.pss.serial.port.core.entity.PoundInfo;
//import com.pss.serial.port.core.exception.BusinessException;
//import com.pss.serial.port.core.utils.SerialportUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.ApplicationContext;
//import org.springframework.stereotype.Component;
//import purejavacomm.*;
//
//import javax.websocket.*;
//import javax.websocket.server.ServerEndpoint;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.UnsupportedEncodingException;
//import java.util.Enumeration;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//@Slf4j
//@ServerEndpoint(value = "/websocket") //接受websocket请求路径
//@Component
//public class SerialPortWebsocket {
//
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    /**
//     * 保存所有在线socket连接
//     */
//    private static Map<String, SerialPortWebsocket> webSocketMap = new LinkedHashMap<>();
//
//    /**
//     * 记录当前在线数目
//     */
//    private static int count = 0;
//
//    /**
//     * 当前连接（每个websocket连入都会创建一个MyWebSocket实例
//     */
//    private Session session;
//
//    /**
//     * 创建监听串口
//     */
//    private static SerialPort serialPort = null;
//
//    /**
//     * 创建监听器
//     */
//    private static SerialPortEventListener serialPortEventListener = null;
//
//    /**
//     * 监听串口
//     */
//    private static String PORT_NAME;
//
//    /**
//     * 监听串口波特率
//     */
//    private static int BAUD_RATE;
//
//    /**
//     * 数据位
//     */
//    private static int DATA_BITS;
//
//    /**
//     * 停止位
//     */
//    private static int STOP_BITS;
//
//    /**
//     * 奇偶位
//     */
//    private static int PARITY;
//
//    /**
//     * 地磅型号
//     */
//    private static String MODEL;
//
//
//    private static ApplicationContext applicationContext;
//
//    public static void setApplicationContext(ApplicationContext applicationContext) {
//        SerialPortWebsocket.applicationContext = applicationContext;
//    }
//
//    private static StringBuffer stringBuffer = new StringBuffer();
//
//    /**
//     * 处理连接建立
//     *
//     * @param session
//     */
//    @OnOpen
//    public void onOpen(Session session) {
//        // 串口打开后的串口对象
//        SerialPort serialPort = null;
//        // 串口的输入流对象
//        InputStream inputStream = null;
//
//        Enumeration<?> en = CommPortIdentifier.getPortIdentifiers();
//        CommPortIdentifier port = null;
//
//        while (en.hasMoreElements()) {
//            CommPortIdentifier portId = (CommPortIdentifier) en.nextElement();
//            // 如果端口类型是串口, 则打印信息
//            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
//                System.out.println(portId.getName());
//                // 选取我们需要的串口端口
//                if (portId.getName().equals("COM1")) {
//                    port = portId;
//                }
//            }
//        }
//
//        if (port == null) {
////            System.out.println("没有获取到需要的串口");
//            log.info("没有获取到需要的串口");
//            throw new BusinessException("500" , "没有获取到串口") ;
//        }
//
//        try {
//            // 打开串口
//            serialPort = (SerialPort) port.open("Main", 2000);
//            // 获取串口的输入流对象
//            inputStream = serialPort.getInputStream();
//        } catch (PortInUseException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
//                    SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
//        } catch (UnsupportedCommOperationException e) {
//            e.printStackTrace();
//        }
//
//        byte[] readB = new byte[21];
//        int nBytes = 0;
//        log.info("[{}]" , "开启websocket , 开始监听数据");
//        while(true) {
//            try {
//                while (inputStream.available() > 0) {
//                    nBytes = inputStream.read(readB);
//                }
//                // 将读出的字符数组数据，直接转换成十六进制。
//                //StringToHex.printHexString(readB);
//                String str = new String(readB) ;
//                System.out.println(str);
//                log.info("接收到的重量为:" + str);
//                sendMessageToAll(new String(readB));
//                log.info("[{}]" , "发送消息成功,消息内容为:" + new String(readB));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * 解析字符串 方法1
//     *
//     * @param bytes 获取的字节码
//     */
//    private void parsingString1(byte[] bytes) {
//        StringBuffer sb = new StringBuffer();
//        //将ASCII码转成字符串
//        for (int i = 0; i < bytes.length; i++) {
//            sb.append((char) Integer.parseInt(String.valueOf(bytes[i])));
//        }
//
//        //解析字符串
//        String[] strs = sb.toString().trim().split("\\+");
//        int weight = 0;
//        for (int j = 0; j < strs.length; j++) {
//            if (strs[j].trim().length() >= 6) {
//                weight = Integer.parseInt(strs[j].trim().substring(0, 6));
//                //发送数据
//                sendMessageToAll(String.valueOf(weight));
//                break;
//            }
//        }
//    }
//
//    /**
//     * 解析字符串 方法2
//     *
//     * @param bytes 获取的字节码
//     */
//    private void parsingString2(byte[] bytes) {
//        StringBuffer sb = new StringBuffer();
//        //将ASCII码转成字符串
//        for (int i = 0; i < bytes.length; i++) {
//            sb.append((char) Integer.parseInt(String.valueOf(bytes[i])));
//        }
//        //解析字符串
//        String[] strs = sb.toString().trim().split("\\+");
//        double weight = 0;
//        for (int j = 0; j < strs.length; j++) {
//            if (strs[j].trim().length() >= 6) {
//                weight = Double.parseDouble(strs[j].trim().substring(0, 6)) / 10;
//                //发送数据
//                sendMessageToAll(String.valueOf(weight));
//                break;
//            }
//        }
//    }
//
//    /**
//     * 解析字符串 方法3
//     *
//     * @param bytes 获取的字节码
//     */
//    private void parsingString3(byte[] bytes) {
//        StringBuffer sb = new StringBuffer();
//        //将ASCII码转成字符串
//        for (int i = 0; i < bytes.length; i++) {
//            sb.append((char) Integer.parseInt(String.valueOf(bytes[i])));
//        }
//
////        logger.info("sb:" + sb.toString());
//        sb.reverse();
//
//        //解析字符串
//        String[] strs = sb.toString().trim().split("\\=");
//        double weight = 0;
//        for (int j = 0; j < strs.length; j++) {
//            if (strs[j].trim().length() >= 6) {
//                weight = Double.parseDouble(strs[j].trim());
//                //发送数据
//                sendMessageToAll(String.valueOf(weight));
//                break;
//            }
//        }
//    }
//
//    /**
//     * 解析字符串 方法3
//     *
//     * @param bytes 获取的字节码
//     */
//    private void parsingString4(byte[] bytes) {
//        StringBuffer sb = new StringBuffer();
//        //将ASCII码转成字符串
//        for (int i = 0; i < bytes.length; i++) {
//            sb.append((char) Integer.parseInt(String.valueOf(bytes[i])));
//        }
//
////        logger.info("sb:" + sb.reverse());
//        //字符串反转
//        sb.reverse();
//
//        //解析字符串
//        String[] strs = sb.toString().trim().split("\\=");
//        int weight = 0;
//        for (int j = 0; j < strs.length; j++) {
//            if (strs[j].trim().length() >= 6) {
//                weight = Integer.parseInt(strs[j].trim().substring(0, 6));
//                //发送数据
//                sendMessageToAll(String.valueOf(weight));
//                break;
//            }
//        }
//    }
//
//    /**
//     * 接受消息
//     *
//     * @param message
//     * @param session
//     */
//    @OnMessage
//    public void onMessage(String message, Session session) {
//        logger.info("收到客户端{}消息：{}", session.getId(), message);
//        try {
//            this.sendMessage(message);
//        } catch (Exception e) {
//            logger.error(e.toString());
//        }
//    }
//
//    /**
//     * 处理错误
//     *
//     * @param error
//     * @param session
//     */
//    @OnError
//    public void onError(Throwable error, Session session) {
//        logger.info("发生错误{},{}", session.getId(), error.getMessage());
//    }
//
//    /**
//     * 处理连接关闭
//     */
//    @OnClose
//    public void onClose() {
////        webSocketMap.remove(this.session.getId());
////        reduceCount();
////        logger.info("连接关闭:{}", this.session.getId());
//
//        //连接关闭后关闭串口，下一次打开连接重新监听串口
//        if (serialPort != null) {
//            SerialportUtil.closePort(serialPort);
//            serialPort = null;
//        }
//    }
//
//    /**
//     * 群发消息
//     *
//     * @param message
//     */
//    public void sendMessageToAll(String message) {
//        for (int i = 0; i < webSocketMap.size(); i++) {
//            try {
////                logger.info("session:id=" + session.getId());
//                this.session.getBasicRemote().sendText(message);
//            } catch (IOException e) {
//                logger.error(e.getMessage());
//            }
//        }
//    }
//
//    /**
//     * 发送消息
//     *
//     * @param message
//     * @throws IOException
//     */
//    public void sendMessage(String message) throws IOException {
////        logger.info("session:id=" + session.getId());
//        this.session.getBasicRemote().sendText(message);
//    }
//
//    //广播消息
//    public static void broadcast() {
//        SerialPortWebsocket.webSocketMap.forEach((k, v) -> {
//            try {
//                v.sendMessage("这是一条测试广播");
//            } catch (Exception e) {
//            }
//        });
//    }
//
//    //获取在线连接数目
//    public static int getCount() {
//        return count;
//    }
//
//    //操作count，使用synchronized确保线程安全
//    public static synchronized void addCount() {
//        SerialPortWebsocket.count++;
//    }
//
//    public static synchronized void reduceCount() {
//        SerialPortWebsocket.count--;
//    }
//
//
//
//}
