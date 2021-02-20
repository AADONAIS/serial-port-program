package com.pss.serial.port.core.websocket;


import com.pss.serial.port.core.controller.WebSocketController;
import com.pss.serial.port.core.exception.BusinessException;
import com.pss.serial.port.core.utils.SpringUtil;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import purejavacomm.CommPortIdentifier;
import purejavacomm.PortInUseException;
import purejavacomm.SerialPort;
import purejavacomm.UnsupportedCommOperationException;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@ServerEndpoint(value = "/websocket") //接受websocket请求路径
@Component
public class SerialWebsocket {


    private static final AtomicInteger OnlineCount = new AtomicInteger(0);
    // concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。
    private static CopyOnWriteArraySet<Session> SessionSet = new CopyOnWriteArraySet<Session>();

    private int size = SpringUtil.getBean(WebSocketController.class).size;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session) {
        SessionSet.add(session);
        int cnt = OnlineCount.incrementAndGet(); // 在线数加1
        log.info("有连接加入，当前连接数为：{}", cnt);
//        SendMessage(session, "连接成功");
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
//            System.out.println("没有获取到需要的串口");
            log.info("没有获取到需要的串口");
            throw new BusinessException("500", "没有获取到串口");
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

        byte[] readAarray = new byte[size];
        log.info("[{}]", "开启websocket , 开始监听数据");
        try {
            while (true) {
//            log.info("into while .....");
                try {
                    StringBuilder stringBuilder = new StringBuilder();
//                while (inputStream.available()!=0) {
//                    int read = inputStream.read();
//                    str += new String(readAarray,0, readAarray.length).trim();
//                }
                    if (inputStream.available() == 0) continue;
                    while (inputStream.available() != 0) {
                        //初始化byte数组
                        inputStream.read(readAarray);
                        stringBuilder.append(new String(readAarray));
                    }
                    if (StringUtils.isBlank(stringBuilder)) continue;

                    // 将读出的字符数组数据，直接转换成十六进制。
//                StringToHex.printHexString(readB);
                    Pattern pattern = Pattern.compile("\\d+.\\d+");
                    Matcher matcher = pattern.matcher(stringBuilder);
                    if (!matcher.find()) {
                        continue;
                    }

                    String str = matcher.group(0);


//                log.info("transform data ....");
                    System.out.println(str);
//                log.info("接收到的重量为:" + str);
                    Thread.sleep(20);
                    SerialWebsocket.SendMessage(session, str);
//                log.info("send message ......");
//                log.info("[{}]" , "发送消息成功,消息内容为:" + new String(readB));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            this.onClose(session);
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        SessionSet.remove(session);
        int cnt = OnlineCount.decrementAndGet();
        log.info("有连接关闭，当前连接数为：{}", cnt);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("来自客户端的消息：{}", message);
        SendMessage(session, "收到消息，消息内容：" + message);

    }

    /**
     * 出现错误
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误：{}，Session ID： {}", error.getMessage(), session.getId());
        error.printStackTrace();
    }

    /**
     * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
     *
     * @param session
     * @param message
     */
    public static void SendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(String.format(message, session.getId()));
        } catch (IOException e) {
            log.error("发送消息出错：{}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 群发消息
     *
     * @param message
     * @throws IOException
     */
    public static void BroadCastInfo(String message) throws IOException {
        for (Session session : SessionSet) {
            if (session.isOpen()) {
                SendMessage(session, message);
            }
        }
    }

    /**
     * 指定Session发送消息
     *
     * @param sessionId
     * @param message
     * @throws IOException
     */
    public static void SendMessage(String sessionId, String message) throws IOException {
        Session session = null;
        for (Session s : SessionSet) {
            if (s.getId().equals(sessionId)) {
                session = s;
                break;
            }
        }
        if (session != null) {
            SendMessage(session, message);
        } else {
            log.warn("没有找到你指定ID的会话：{}", sessionId);
        }
    }
}
