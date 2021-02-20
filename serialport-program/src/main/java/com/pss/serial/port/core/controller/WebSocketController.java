package com.pss.serial.port.core.controller;

import com.pss.serial.port.core.websocket.SerialWebsocket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/ws")
public class WebSocketController {

    @Value("${buffer.size}")
    public int size;

    @Value("${baud.rate}")
    private String baudRate ;


    /**
     * 群发消息内容
     * @param message
     * @return
     */
    @RequestMapping(value="/sendAll", method= RequestMethod.GET)
    String sendAllMessage(@RequestParam(required=true) String message){
        try {
            SerialWebsocket.BroadCastInfo(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ok";
    }


    /**
     * 指定会话ID发消息
     * @param message 消息内容
     * @param id 连接会话ID
     * @return
     */
    @RequestMapping(value="/sendOne", method=RequestMethod.GET)
    String sendOneMessage(@RequestParam(required=true) String message,@RequestParam(required=true) String id){
        try {
            SerialWebsocket.SendMessage(id,message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ok";
    }
}